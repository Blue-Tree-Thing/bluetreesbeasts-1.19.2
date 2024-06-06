package net.fabricmc.bluetreebeasts.block.custom;

import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.entities.custom.GannetWhaleEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class GannetWhaleEggBlock extends Block {
    public static final IntProperty HATCH = Properties.HATCH;
    public static final IntProperty AGE = IntProperty.of("age", 0, 200); // 10 seconds * 20 ticks


    public GannetWhaleEggBlock() {
        super(Settings.of(Material.EGG)
                .ticksRandomly()
                .strength(0.5F)
                .sounds(BlockSoundGroup.CALCITE)
                .nonOpaque());
        this.setDefaultState(this.stateManager.getDefaultState().with(HATCH, 0).with(AGE, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HATCH, AGE);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient && hand == Hand.MAIN_HAND) {
            int hatch = state.get(HATCH);
            if (hatch < 2) {
                world.setBlockState(pos, state.with(HATCH, hatch + 1));
                return ActionResult.SUCCESS;
            } else {
                hatchGannetWhale(world, pos);
                world.removeBlock(pos, false);
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        int age = state.get(AGE);
        if (age < 200) {
            world.setBlockState(pos, state.with(AGE, age + 1), 4);
        } else {
            hatchGannetWhale(world, pos);
            world.removeBlock(pos, false);
        }
    }

    private void hatchGannetWhale(World world, BlockPos pos) {
        GannetWhaleEntity whale = ModEntities.GANNETWHALE.create(world);
        if (whale != null) {
            whale.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
            whale.setBaby(true);
            whale.setBreedingAge(-18000); // Grows up in 15 minutes
            world.spawnEntity(whale);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.25f, 0.0f, 0.25f, 0.75f, 0.50f, 0.75f); // A smaller, egg-shaped hitbox
    }


}