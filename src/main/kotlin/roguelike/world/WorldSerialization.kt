package roguelike.world

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D
import roguelike.blocks.GameBlock
import roguelike.builders.EntityFactory
import roguelike.builders.GameTileRepository
import roguelike.extensions.GameEntity

object Position3DSerializer : KSerializer<Position3D> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Position3D", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Position3D) =
        encoder.encodeString("${value.x},${value.y},${value.z}")

    override fun deserialize(decoder: Decoder): Position3D {
        val (x, y, z) = decoder.decodeString().split(',').map { it.toInt() }
        return Position3D.create(x, y, z)
    }
}

object GameBlockSerializer : KSerializer<GameBlock> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("GameBlock", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: GameBlock) {
        encoder.encodeSerializableValue(ListSerializer(GameEntitySerializer), value.entities)
        encoder.encodeBoolean(value.isFloor)
    }

    override fun deserialize(decoder: Decoder): GameBlock {
        val entities = decoder.decodeSerializableValue(ListSerializer(GameEntitySerializer)).toMutableList()
        val isFloor = decoder.decodeBoolean()
        return GameBlock(
            if (isFloor) GameTileRepository.FLOOR else GameTileRepository.WALL,
            entities
        )
    }
}

object GameEntitySerializer : KSerializer<GameEntity<EntityType>> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("GameEntity", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): GameEntity<EntityType> {
        return EntityFactory.newPlayer()
    }

    override fun serialize(encoder: Encoder, value: GameEntity<EntityType>) {
    }
}