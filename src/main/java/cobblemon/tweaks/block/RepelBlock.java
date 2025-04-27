package cobblemon.tweaks.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.Level;

import java.util.List;

public class RepelBlock extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;


    public RepelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean moved) {
        if (!world.isClientSide) {
            boolean hasSignal = world.hasNeighborSignal(pos);
            if (state.getValue(POWERED) != hasSignal) {
                world.setBlock(pos, state.setValue(POWERED, hasSignal), 3);
            }
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    public void appendHoverText(ItemStack arg, Item.TooltipContext arg2, List<Component> listComponent, TooltipFlag arg3) {
        listComponent.add(Component.translatable("tooltip.createcobblemontweaks.repel_block"));
        super.appendHoverText(arg, arg2, listComponent, arg3);
    }
}
