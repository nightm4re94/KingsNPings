package me.rochblondiaux.kingsnpigs.entities.environment.furniture;

import de.gurkenlabs.litiengine.entities.AnimationInfo;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.Prop;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import lombok.Getter;
import lombok.Setter;
import me.rochblondiaux.kingsnpigs.entities.animation.DoorAnimationController;

@Setter
@Getter
@EntityInfo(renderType = RenderType.SURFACE)
@AnimationInfo(spritePrefix = "prop-door")
public class Door extends Prop {

    private State doorState;

    public Door() {
        super("door");
        this.doorState = State.CLOSED;

        getHitPoints().setMax(3);
        getHitPoints().setValue(3);
    }



    @Override
    protected IEntityAnimationController<?> createAnimationController() {
        return new DoorAnimationController(this);
    }

    public enum State {
        OPENING,
        CLOSING,
        CLOSED
    }
}
