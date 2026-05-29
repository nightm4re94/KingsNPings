package me.rochblondiaux.kingsnpigs.entities.environment.collectible;

import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.graphics.RenderType;

@CollisionInfo(collision = false, collisionBoxHeight = 14, collisionBoxWidth = 14, align = Align.RIGHT)
@EntityInfo(renderType = RenderType.OVERLAY, height = 14, width = 14)
public class Heart extends Collectible {

    public Heart() {
        super("heart", player -> player.getHitPoints().setValue(player.getHitPoints().getValue() + 1));
        setWidth(14);
        setHeight(14);
    }

}
