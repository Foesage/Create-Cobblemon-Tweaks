package cobblemon.creatified.block;

import cobblemon.creatified.network.ModNetwork;
import cobblemon.creatified.network.packet.RepelRangeParticlesPacket;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
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

public class RepelBlock extends Block {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public static final BooleanProperty SHOWING_RANGE = BooleanProperty.create("showing_range");

    private static final int BLOCKING_RADIUS = 64;

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
            boolean newRange = !state.getValue(SHOWING_RANGE);

            if (!level.isClientSide) {
                BlockState updated = state.setValue(SHOWING_RANGE, newRange);
                level.setBlock(pos, updated, 3);

                player.displayClientMessage(
                        Component.literal(newRange ? "Showing Repel range." : "Repel range indicator disabled."),
                        true
                );

                if (newRange && level instanceof ServerLevel serverLevel) {
                    int redstonePower = serverLevel.getBestNeighborSignal(pos);
                    int effectiveRadius = Math.max(1, (int)(BLOCKING_RADIUS * (1.0 - redstonePower / 15.0)));

                    for (ServerPlayer serverPlayer : serverLevel.players()) {
                        if (serverPlayer.blockPosition().closerThan(pos, BLOCKING_RADIUS)) {
                            ModNetwork.sendTo(serverPlayer, new RepelRangeParticlesPacket(pos, effectiveRadius));
                        }
                    }
                }
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
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED)) {
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.3;
            double y = pos.getY() + 1.0 + random.nextDouble() * 0.5;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.3;
            double xSpeed = (random.nextDouble() - 0.5) * 0.02;
            double ySpeed = 0.07;
            double zSpeed = (random.nextDouble() - 0.5) * 0.02;

            level.addAlwaysVisibleParticle(ParticleTypes.SMOKE, true, x, y, z, xSpeed, ySpeed, zSpeed);
        }

        if (state.getValue(SHOWING_RANGE)) {
            double centerX = pos.getX() + 0.5;
            double centerY = pos.getY() + 1.0;
            double centerZ = pos.getZ() + 0.5;

            for (int i = 0; i < 100; i++) {
                double angle = 2 * Math.PI * i / 100;
                double x = centerX + BLOCKING_RADIUS * Math.cos(angle);
                double z = centerZ + BLOCKING_RADIUS * Math.sin(angle);
                level.addParticle(ParticleTypes.END_ROD, x, centerY, z, 0, 0, 0);
            }
        }
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
}
