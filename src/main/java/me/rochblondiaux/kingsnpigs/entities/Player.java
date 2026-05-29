package me.rochblondiaux.kingsnpigs.entities;

import java.awt.geom.Rectangle2D;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.*;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.physics.Collision;
import de.gurkenlabs.litiengine.physics.IMovementController;
import lombok.Getter;
import lombok.Setter;
import me.rochblondiaux.kingsnpigs.entities.ability.JumpAbility;
import me.rochblondiaux.kingsnpigs.entities.animation.PlayerAnimationController;
import me.rochblondiaux.kingsnpigs.entities.controller.PlayerMovementController;
import me.rochblondiaux.kingsnpigs.entities.emitter.FallParticleEmitter;
import me.rochblondiaux.kingsnpigs.entities.emitter.WalkParticleSpawner;

@EntityInfo(width = 35, height = 40)
@CollisionInfo(collision = true, collisionBoxWidth = 20, collisionBoxHeight = 39)
@MovementInfo(velocity = 250)
@AnimationInfo(spritePrefix = "player")
@CombatInfo(hitpoints = 3)
@Getter
public class Player extends Creature implements IUpdateable {

    private static Player instance;
    private static final int MAX_ADDITIONAL_JUMPS = 1;

    private final JumpAbility jumpAbility;

    // State
    @Setter
    private int diamonds = 0;
    private int consecutiveJumps = 0;
    private boolean onGround = false;
    @Setter
    private boolean isEnteringDoor = false, isExitingDoor = false;
    @Setter
    private boolean controlsEnabled;

    public Player() {
        super("player");
        if (instance != null)
            throw new IllegalStateException("There can only be one player.");
        else
            instance = this;

        this.jumpAbility = new JumpAbility(this);
        this.controlsEnabled = true;

        animations().scaleSprite(1.5f);

        onMoved(new WalkParticleSpawner());
    }

    @Override
    public void update() {
        boolean isTouchingGround = isTouchingGround();
        if (!this.onGround && isTouchingGround) {
            animations().play("player-ground-%s".formatted(getFacingDirection().toString().toLowerCase()));


            Game.world().environment().add(new FallParticleEmitter(this));
        }
        onGround = isTouchingGround;

        if (this.onGround)
            this.consecutiveJumps = 0;
    }

    @Action(description = "This performs the jump ability for the player's entity.")
    public void jump() {
        if (this.consecutiveJumps >= MAX_ADDITIONAL_JUMPS || !this.jumpAbility.canCast())
            return;

        this.jumpAbility.cast();
        this.consecutiveJumps++;
    }

    private boolean isTouchingGround() {
        Rectangle2D groundCheck = new Rectangle2D.Double(this.getCollisionBox().getX(), this.getCollisionBox().getY(), this.getCollisionBoxWidth(), this.getCollisionBoxHeight() + 1);
        if (groundCheck.getMaxY() > Game.physics().getBounds().getMaxY())
            return true;
        return Game.physics().collides(groundCheck, Collision.STATIC);
    }


    public static Player get() {
        if (instance == null)
            instance = new Player();
        return instance;
    }

    @Override
    protected IMovementController createMovementController() {
        // setup movement controller
        return new PlayerMovementController(this);
    }

    @Override
    protected IEntityAnimationController<? extends Creature> createAnimationController() {
        return new PlayerAnimationController(this);
    }

}
