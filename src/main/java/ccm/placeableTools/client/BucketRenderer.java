package ccm.placeableTools.client;

import ccm.placeableTools.block.BucketTE;
import ccm.placeableTools.client.renderCrap.Point2D;
import ccm.placeableTools.client.renderCrap.Quad;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BucketRenderer extends TileEntitySpecialRenderer
{
    private static final String MODEL = "/assets/placeabletools/models/bucket.obj";
    private static final IModelCustom modelBucket = AdvancedModelLoader.loadModel(MODEL);
    private static final ResourceLocation texture = new ResourceLocation("placeabletools", "/textures/bucket.png");
    private static final Quad[] octagon = new Quad[] {
            new Quad(new Point2D(1, 5),  new Point2D(1, 11),  new Point2D(5, 15),  new Point2D(5, 1)),
            new Quad(new Point2D(5, 1),  new Point2D(5, 15),  new Point2D(11, 15), new Point2D(11, 1)),
            new Quad(new Point2D(11, 1), new Point2D(11, 15), new Point2D(15, 11), new Point2D(15, 5)),
    };

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f)
    {
        BucketTE bucketTE = (BucketTE) te;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y, (float) z + 1F);
        GL11.glScalef(0.0625F, 0.0625F, 0.0625F);

        bindTexture(texture);
        modelBucket.renderAllExcept("Liquid_001");
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
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            Icon icon = bucketTE.getFluid().getIcon();
            for (Quad quad : octagon) for (Point2D point : quad.getPoints()) tessellator.addVertexWithUV(point.getU(), 15, point.getV(), icon.getInterpolatedU(point.getU()), icon.getInterpolatedV(point.getV()));
            tessellator.draw();

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }
}
