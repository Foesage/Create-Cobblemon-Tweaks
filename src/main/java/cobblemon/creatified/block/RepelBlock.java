package cobblemon.creatified.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class RepelBlock extends Block {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty SHOWING_RANGE = BooleanProperty.create("showing_range");

    // Match your real repel logic radius here
    private static final int REPEL_RADIUS = 64;

    public RepelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, Boolean.FALSE)
                .setValue(SHOWING_RANGE, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, SHOWING_RANGE);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean moved) {
        if (!level.isClientSide) {
            boolean hasSignal = level.hasNeighborSignal(pos);
            if (state.getValue(POWERED) != hasSignal) {
                level.setBlock(pos, state.setValue(POWERED, hasSignal), 3);
            }
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }


    public void appendHoverText(ItemStack stack, BlockGetter getter, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cobblemoncreatified.repel_block"));
    }


    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            boolean currentlyShowing = state.getValue(SHOWING_RANGE);
            level.setBlock(pos, state.setValue(SHOWING_RANGE, !currentlyShowing), 3);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(SHOWING_RANGE)) {
            double centerX = pos.getX() + 0.5;
            double centerY = pos.getY() + 1.0;
            double centerZ = pos.getZ() + 0.5;

            for (double angle = 0; angle < 360; angle += 30) { // Spawns a ring every 30°
                double rad = Math.toRadians(angle);
                double x = centerX + REPEL_RADIUS * Math.cos(rad);
                double z = centerZ + REPEL_RADIUS * Math.sin(rad);

                level.addParticle(
                        ParticleTypes.END_ROD,
                        x, centerY, z,
                        0, 0, 0
                );
            }
        }
    }
}
