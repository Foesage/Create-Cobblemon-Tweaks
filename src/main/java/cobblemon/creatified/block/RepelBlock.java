package cobblemon.creatified.block;

import cobblemon.creatified.block.entity.RepelBlockEntity;
import cobblemon.creatified.tracker.ActiveRepelBlockTracker;
import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormEntityParticlePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

public class RepelBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public static final BooleanProperty SHOWING_RANGE = BooleanProperty.create("showing_range");

    public static final int BLOCKING_RADIUS = 64;

    private static final VoxelShape SHAPE = Shapes.box(
            5.0 / 16.0, 0.0, 5.0 / 16.0,
            11.0 / 16.0, 7.0 / 16.0, 11.0 / 16.0
    );

    public RepelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, Boolean.TRUE)
                .setValue(SHOWING_RANGE, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, SHOWING_RANGE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(POWERED, Boolean.TRUE)
                .setValue(SHOWING_RANGE, Boolean.FALSE);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return false;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            ActiveRepelBlockTracker.ACTIVE_BLOCKS.add(pos.immutable());

            if (state.getValue(POWERED) && !oldState.is(state.getBlock()) && level instanceof ServerLevel serverLevel) {
                for (ServerPlayer player : serverLevel.players()) {
                    if (player.blockPosition().closerThan(pos, BLOCKING_RADIUS)) {
                        serverLevel.playSound(null, pos, CobblemonSounds.MEDICINE_SPRAY_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
                        SpawnSnowstormEntityParticlePacket packet = new SpawnSnowstormEntityParticlePacket(
                                cobblemonResource("heavy_collapse_smoke"),
                                player.getId(),
                                List.of("heavy_collapse_smoke", "middle"),
                                null,
                                null
                        );
                        packet.sendToPlayer(player);
                        serverLevel.sendParticles(player, ParticleTypes.CAMPFIRE_COSY_SMOKE, true, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 10, 0.2, 0.1, 0.2, 0.01);
                    }
                }
            }
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide) {
            ActiveRepelBlockTracker.ACTIVE_BLOCKS.remove(pos.immutable());
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {

        boolean isEmptyHand = player.getMainHandItem().isEmpty();
        boolean isCrouching = player.isCrouching();

        if (isEmptyHand && isCrouching) {
            boolean currentlyActive = state.getValue(SHOWING_RANGE);

            if (!level.isClientSide) {
                for (BlockPos otherPos : ActiveRepelBlockTracker.ACTIVE_BLOCKS) {
                    if (!otherPos.equals(pos) && otherPos.closerThan(pos, BLOCKING_RADIUS * 2)) {
                        BlockState otherState = level.getBlockState(otherPos);
                        if (otherState.getBlock() instanceof RepelBlock && otherState.getValue(SHOWING_RANGE)) {
                            level.setBlock(otherPos, otherState.setValue(SHOWING_RANGE, false), 3);
                        }
                    }
                }
                BlockState updated = state.setValue(SHOWING_RANGE, !currentlyActive);
                level.setBlock(pos, updated, 3);

                player.displayClientMessage(
                        Component.literal(!currentlyActive ? "Showing Repel range." : "Repel range indicator disabled."),
                        true
                );
            } else {
                player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.8f, 1.4f);
            }

            return ItemInteractionResult.CONSUME;
        }

        if (isEmptyHand && !isCrouching) {
            boolean wasPowered = state.getValue(POWERED);
            boolean turningOn = !wasPowered;

            BlockState newState = state.setValue(POWERED, turningOn);

            if (!level.isClientSide) {
                level.setBlock(pos, newState, 3);

                if (turningOn && player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, pos, CobblemonSounds.MEDICINE_SPRAY_USE, SoundSource.BLOCKS, 2.0f, 0.8f);
                    SpawnSnowstormEntityParticlePacket packet = new SpawnSnowstormEntityParticlePacket(
                            cobblemonResource("heavy_collapse_smoke"),
                            serverPlayer.getId(),
                            List.of("heavy_collapse_smoke", "middle"),
                            null,
                            null
                    );
                    packet.sendToPlayer(serverPlayer);

                    double x = pos.getX() + 0.5;
                    double y = pos.getY() + 1.0;
                    double z = pos.getZ() + 0.5;
                    serverLevel.sendParticles(serverPlayer, ParticleTypes.CAMPFIRE_COSY_SMOKE, true, x, y, z, 10, 0.2, 0.1, 0.2, 0.01);
                }
            }

            if (level.isClientSide && player != null) {
                player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.8F, turningOn ? 1.2F : 0.8F);
            }

            return ItemInteractionResult.CONSUME;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RepelBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof RepelBlockEntity repelBe) repelBe.tickServer();
        };
    }
}
