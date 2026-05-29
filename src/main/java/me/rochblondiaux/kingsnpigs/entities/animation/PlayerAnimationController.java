package me.rochblondiaux.kingsnpigs.entities.animation;

import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.AnimationListener;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;
import de.gurkenlabs.litiengine.resources.Resources;
import me.rochblondiaux.kingsnpigs.entities.Player;

public class PlayerAnimationController extends CreatureAnimationController<Player> {

    public PlayerAnimationController(Player creature) {
        super(creature, true);

        // Custom animations
        registerAnimations("jump");
        registerAnimations("fall");
        registerAnimations("ground");
        registerAnimations("doorin");
        registerAnimations("doorout");
        registerAnimations("attack");
        registerAnimations("hit");
        registerAnimations("dead");

        // Rules
        addRule(player -> player.movement().getVelocity() == 0, player -> "player-idle-%s".formatted(player.getFacingDirection().toString().toLowerCase()));
        addRule(Player::isExitingDoor, player -> "player-doorout-%s".formatted(player.getFacingDirection().toString().toLowerCase()));
        addRule(Player::isEnteringDoor, player -> "player-doorin-%s".formatted(player.getFacingDirection().toString().toLowerCase()));
        addRule(player -> player.getJumpAbility().isActive(), player -> "player-jump-%s".formatted(player.getFacingDirection().toString().toLowerCase()));
        addRule(player -> !player.isOnGround(), player -> "player-fall-%s".formatted(player.getFacingDirection().toString().toLowerCase()));

        addListener(new AnimationListener() {
            @Override
            public void finished(Animation animation) {
                final Player player = PlayerAnimationController.this.getEntity();
                if (animation.getName().contains("doorin") && player.isEnteringDoor())
                    player.setEnteringDoor(false);
                else if (animation.getName().contains("doorout") && player.isExitingDoor())
                    player.setExitingDoor(false);
            }
        });
    }

    private void registerAnimations(String action) {
        Spritesheet spritesheet = Resources.spritesheets().get("player-%s-right".formatted(action));
        Animation animation = new Animation("player-%s-right".formatted(action), spritesheet, false, false);

        add(animation);
        add(flippedAnimation(animation, "player-%s-left".formatted(action),false));
    }

    @Override
    public boolean isPlaying(String animationName) {
        return this.getCurrent() != null && this.getCurrent().getName() != null && (this.getCurrent().getName().equalsIgnoreCase(animationName) && this.getCurrent().getName().contains(animationName));
    }
}
