package net.fabricmc.bluetreebeasts.block.custom;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class WreathBlock extends HorizontalFacingBlock {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(1.0D, 1.0D, 15.0D, 15.0D, 15.0D, 16.0D);
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(1.0D, 1.0D, 0.0D, 15.0D, 15.0D, 1.0D);
    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(15.0D, 1.0D, 1.0D, 16.0D, 15.0D, 15.0D);
    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0D, 1.0D, 1.0D, 1.0D, 15.0D, 15.0D);

    public WreathBlock() {
        super(FabricBlockSettings.of(Material.DECORATION).strength(.25f).noCollision().nonOpaque());
        setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // Start by trying to place the wreath based on the horizontal direction the player is facing.
        Direction preferredDirection = ctx.getPlayerFacing().getOpposite();
        if (preferredDirection.getAxis().isHorizontal()) {
            if (canPlace(ctx, preferredDirection)) {
                return this.getDefaultState().with(FACING, preferredDirection);
            }
        }

        // If the preferred direction didn't work, check the other horizontal directions.
        for (Direction direction : Direction.values()) {
            if (direction.getAxis().isHorizontal() && direction != preferredDirection) { // Avoid rechecking the preferred direction
                if (canPlace(ctx, direction)) {
                    return this.getDefaultState().with(FACING, direction);
                }
            }
        }

        return null; // Return null if no suitable direction found
    }

    private boolean canPlace(ItemPlacementContext ctx, Direction direction) {
        BlockPos blockPos = ctx.getBlockPos().offset(direction.getOpposite());
        return ctx.getWorld().getBlockState(blockPos).isSideSolidFullSquare(ctx.getWorld(), blockPos, direction);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> VoxelShapes.fullCube();
        };
    }
}