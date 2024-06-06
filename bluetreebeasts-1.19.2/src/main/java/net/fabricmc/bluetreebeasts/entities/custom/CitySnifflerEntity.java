package net.fabricmc.bluetreebeasts.entities.custom;

import net.fabricmc.bluetreebeasts.block.entity.SnifflerColonyEnterBlockEntity;
import net.fabricmc.bluetreebeasts.entities.custom.ai.*;
import net.fabricmc.bluetreebeasts.items.ModItems;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;


import java.util.List;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.PLAY_ONCE;

public class CitySnifflerEntity extends AnimalEntity implements IAnimatable {

    public static final TrackedData<Boolean> INSIDE_NEST = DataTracker.registerData(CitySnifflerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int seedCount;
    private int produceCount;
    @Nullable
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    protected static final AnimationBuilder walking_animation = new AnimationBuilder().addAnimation("animation.city_sniffler.walk", LOOP);
    private static final AnimationBuilder foraging_animation = new AnimationBuilder().addAnimation("animation.city_sniffler.gather", PLAY_ONCE);
    private static final AnimationBuilder idle_animation = new AnimationBuilder().addAnimation("animation.city_sniffler.idle", LOOP);


    public CitySnifflerEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.seedCount = 0;
        this.setPersistent();
        this.checkDespawn();
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 5)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .5f)
                .add(EntityAttributes.GENERIC_ARMOR, 1f);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(INSIDE_NEST, false);
    }



    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new LookAtEntityGoal(this, LivingEntity.class, 20.0f));
        this.goalSelector.add(2, new SwimGoal(this));
        this.goalSelector.add(8, new EscapeDangerGoal(this, .8));
            this.goalSelector.add(6, new CitySnifflerForageGoal(this));
            this.goalSelector.add(5, new CustomLandMobWanderGoal(this, .5, 60, 3));
            this.goalSelector.add(4, new CitySnifflerConstructNestGoal(this));
            this.goalSelector.add(3, new CitySnifflerEnterNestGoal(this));
        this.targetSelector.add(7, new AnimalMateGoal(this,1));
    }


    @Override
    public void tick() {
        super.tick();
        BlockPos currentPos = this.getBlockPos();
        BlockPos blockEntityPos = currentPos.down();  // Assuming the nest block entity is directly beneath the entity
        World world = this.getWorld();
        BlockEntity blockEntity = world.getBlockEntity(blockEntityPos);

        if (blockEntity instanceof SnifflerColonyEnterBlockEntity) {
            SnifflerColonyEnterBlockEntity nestBlockEntity = (SnifflerColonyEnterBlockEntity) blockEntity;
            if (this.getDataTracker().get(INSIDE_NEST) && !this.isRemoved()) {
                nestBlockEntity.handleSnifflerEntry(this);
                this.remove(RemovalReason.DISCARDED); // Use the appropriate RemovalReason if needed
            }
        }
    }

    public void pickUpItem(ItemStack itemStack) {
        if (itemStack.getItem() == Items.WHEAT_SEEDS && itemStack.getCount() > 0) {
            seedCount += 1;  // Only add one seed to the seed count
            itemStack.decrement(1);  // Decrease the stack size by one
            System.out.println("Picked up a seed: Current seed count = " + seedCount);
        } else if (itemStack.getItem() == ModItems.SNIFFLERPRODUCE && itemStack.getCount() > 0) {
            produceCount += 1;  // Only add one produce item to the count
            itemStack.decrement(1);  // Decrease the stack size by one
            System.out.println("Picked up a produce item: Current produce count = " + produceCount);
        }
    }

    public void decrementProduceCount() {
        if (produceCount > 0) produceCount--;
    }



    public boolean enablePoison = false; // This flag determines if the poison effect should be active

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsPoisonous", this.enablePoison);
        nbt.putInt("SeedCount", this.seedCount);
        nbt.putInt("ProduceCount", this.produceCount);
        nbt.putBoolean("IsForaging", this.isForaging);
        nbt.putBoolean("InsideNest", this.dataTracker.get(INSIDE_NEST));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.enablePoison = nbt.getBoolean("IsPoisonous");
        this.seedCount = nbt.getInt("SeedCount");
        this.produceCount = nbt.getInt("ProduceCount");
        this.isForaging = nbt.getBoolean("IsForaging");
        this.dataTracker.set(INSIDE_NEST, nbt.getBoolean("InsideNest"));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.isAlive() && this.enablePoison) {
            // Get entities around this entity but exclude itself from being affected by the poison
            List<PlayerEntity> list = this.world.getEntitiesByClass(PlayerEntity.class, this.getBoundingBox().expand(0.3),
                    entity -> entity != null && !entity.isSpectator() && entity.isAlive());
            for (PlayerEntity player : list) {
                applyPoisonEffect(player);
            }
            List<HostileEntity> listB = this.world.getEntitiesByClass(HostileEntity.class, this.getBoundingBox().expand(0.3),
                    entity -> entity != null && !entity.isSpectator() && entity.isAlive());
            for (HostileEntity mobEntity : listB) {
                applyPoisonEffect(mobEntity);
            }
        }
    }

    public void applyPoisonEffect(LivingEntity target) {
        if (target.damage(DamageSource.mob(this), 0)) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 20 , 2), this);
            this.playSound(SoundEvents.ENTITY_PUFFER_FISH_STING, 1.0f, 1.0f);
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() == ModItems.SNIFFLERPRODUCE) {
            if (!this.world.isClient) {
                this.enablePoison = true; // Enable poison effect
                if (!player.isCreative()) {
                    itemStack.decrement(1);
                }
                return ActionResult.SUCCESS;
            }
        }
        return super.interactMob(player, hand);
    }

    public void decreaseSeedCount() {
        if (seedCount > 0) seedCount--;
    }

    public boolean hasSeeds() {
        return seedCount > 0;
    }

    public boolean hasProduce() {
        return produceCount > 0;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }




    public boolean isInsideNest() {
        return this.dataTracker.get(INSIDE_NEST);
    }

    public boolean canEnterNest() {
        boolean isNight = this.world.getTimeOfDay() >= 13000 && this.world.getTimeOfDay() <= 23000;
        boolean hasSeedOrProduce = this.hasSeeds() || this.hasProduce();

        return isNight || hasSeedOrProduce && !this.isInsideNest();
    }

    private void setAnimationWithTransition(AnimationController<CitySnifflerEntity> controller, AnimationBuilder animation, int transitionTicks) {
        controller.transitionLengthTicks = transitionTicks;
        controller.setAnimation(animation);
    }


    public boolean isForaging = false;

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AnimationController<CitySnifflerEntity> controller = (AnimationController<CitySnifflerEntity>) event.getController();
        boolean isMoving = event.isMoving();
        boolean isForaging = this.isForaging;

        String targetAnimation;
        int transitionTicks;

        if (isForaging) {
            targetAnimation = "animation.city_sniffler.gather";
            transitionTicks = 5; // Custom transition length for foraging animation
        } else if (isMoving) {
            targetAnimation = "animation.city_sniffler.walk";
            transitionTicks = 7; // Custom transition length for walking animation
        } else {
            targetAnimation = "animation.city_sniffler.idle";
            transitionTicks = 2; // Custom transition length for idle animation
        }

        String currentAnimationName = controller.getCurrentAnimation() != null ? controller.getCurrentAnimation().animationName : "";
        if (!currentAnimationName.equals(targetAnimation)) {
            switch (targetAnimation) {
                case "animation.city_sniffler.gather" ->
                        setAnimationWithTransition(controller, foraging_animation, transitionTicks);
                case "animation.city_sniffler.walk" ->
                        setAnimationWithTransition(controller, walking_animation, transitionTicks);
                case "animation.city_sniffler.idle" ->
                        setAnimationWithTransition(controller, idle_animation, transitionTicks);
            }
        }

        return PlayState.CONTINUE;
    }

    public void setForaging(boolean foraging) {
        this.isForaging = foraging;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this,"controller", 4, this::predicate));
    }

    @Override
    public @Nullable AnimationFactory getFactory() {
        return factory;
    }
}
