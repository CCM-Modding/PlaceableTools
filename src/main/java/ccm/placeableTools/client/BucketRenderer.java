package ccm.placeableTools.client;

import ccm.nucleumOmnium.client.renderShapes.Point3D;
import ccm.nucleumOmnium.client.renderShapes.complexShapes.Octagon;
import ccm.placeableTools.block.BucketTE;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BucketRenderer extends TileEntitySpecialRenderer
{
    private static final String           MODEL          = "/assets/placeabletools/models/bucket.obj";
    private static final IModelCustom     BUCKET_MODEL   = AdvancedModelLoader.loadModel(MODEL);
    private static final ResourceLocation BUCKET_TEXTURE = new ResourceLocation("placeabletools", "/textures/bucket.png");
    private static final Octagon          OCTAGON        = new Octagon(new Point3D(8, 15, 8), 7);

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f)
    {
        BucketTE bucketTE = (BucketTE) te;
        GL11.glPushMatrix();
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y, (float) z + 1F);
        GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        bindTexture(BUCKET_TEXTURE);
        BUCKET_MODEL.renderAllExcept("Liquid_001");

        GL11.glPopAttrib();
        GL11.glPopMatrix();

        if (bucketTE.getFluid() != null)
        {
            GL11.glPushMatrix();
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float) x, (float) y, (float) z);
            GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            bindTexture(TextureMap.locationBlocksTexture);
            OCTAGON.renderShapeWithIcon(bucketTE.getFluid().getIcon());

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }
}
