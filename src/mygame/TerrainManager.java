package mygame;

import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;
import jme3tools.converters.ImageToAwt;

/**
 *
 * @author wasd
 */
public class TerrainManager {

    private TerrainQuad terrain;
    private WaterFilter water;
    private FilterPostProcessor waterFilter;
    private Game app;
    private Vector3f lightDir = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);

    public TerrainManager(Game app) {
        this.app = app;
    }

    public TerrainQuad getTerrain() {
        if (terrain == null) {
            initTerrain();
        }
        return terrain;
    }

    public FilterPostProcessor getWater() {
        if (water == null) {
            initWater();
        }
        return waterFilter;
    }

    private void initTerrain() {
        //TODO this is an example terrain. I should make my own

        /** 1. Create terrain material and load four textures into it. */
        Material mat_terrain = new Material(app.getAssetManager(),
                "Common/MatDefs/Terrain/Terrain.j3md");

        /** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
        mat_terrain.setTexture("Alpha", app.getAssetManager().loadTexture(
                "Textures/Terrain/alphamap.png"));

        /** 1.2) Add GRASS texture into the red layer (Tex1). */
        Texture grass = app.getAssetManager().loadTexture(
                "Textures/Terrain/grass.jpg");
        grass.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex1", grass);
        mat_terrain.setFloat("Tex1Scale", 64f);

        /** 1.3) Add DIRT texture into the green layer (Tex2) */
        Texture dirt = app.getAssetManager().loadTexture(
                "Textures/Terrain/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex2", dirt);
        mat_terrain.setFloat("Tex2Scale", 32f);

        /** 1.4) Add ROAD texture into the blue layer (Tex3) */
        Texture rock = app.getAssetManager().loadTexture(
                "Textures/Terrain/road.jpg");
        rock.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex3", rock);
        mat_terrain.setFloat("Tex3Scale", 128f);

        /** 2. Create the height map */
        AbstractHeightMap heightmap = null;
        Texture heightMapImage = app.getAssetManager().loadTexture(
                "Textures/Terrain/heightmap.png");
//        heightmap = new ImageBasedHeightMap(
//                ImageToAwt.convert(heightMapImage.getImage(), false, true, -1));
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();
        heightmap.smooth(.9f);

        /** 3. We have prepared material and heightmap. 
         * Now we create the actual terrain:
         * 3.1) Create a TerrainQuad and name it "my terrain".
         * 3.2) A good value for terrain tiles is 64x64 -- so we supply 64+1=65.
         * 3.3) We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
         * 3.4) As LOD step scale we supply Vector3f(1,1,1).
         * 3.5) We supply the prepared heightmap itself.
         */
        terrain = new TerrainQuad("my terrain", 64, 1025, heightmap.getHeightMap());

        /** 4. We give the terrain its material, position & scale it, and attach it. */
        terrain.setMaterial(mat_terrain);
        terrain.setLocalTranslation(0, -30, 0);
        terrain.setLocalScale(.3f, .2f, .3f);

        /** 5. The LOD (level of detail) depends on were the camera is: */
        TerrainLodControl control = new TerrainLodControl(terrain, app.getCamera());
        terrain.addControl(control);
    }

    private void initWater() {
        water = new WaterFilter(app.getRootNode(), lightDir);

        waterFilter = new FilterPostProcessor(app.getAssetManager());

        waterFilter.addFilter(water);
        BloomFilter bloom = new BloomFilter();
        //bloom.getE
        bloom.setExposurePower(55);
        bloom.setBloomIntensity(1.0f);
        waterFilter.addFilter(bloom);
        LightScatteringFilter lsf = new LightScatteringFilter(lightDir.mult(-300));
        lsf.setLightDensity(1.0f);
        waterFilter.addFilter(lsf);
        DepthOfFieldFilter dof = new DepthOfFieldFilter();
        dof.setFocusDistance(0);
        dof.setFocusRange(100);
        waterFilter.addFilter(dof);


        water.setWaveScale(0.002f);
        water.setMaxAmplitude(3f);
        water.setFoamExistence(new Vector3f(1f, 4, 0.5f));
        water.setFoamTexture((Texture2D) app.getAssetManager().loadTexture("Common/MatDefs/Water/Textures/foam2.jpg")); //foam to foam3
        //water.setNormalScale(0.5f);

        //water.setRefractionConstant(0.25f);
        water.setRefractionStrength(0.2f);
        //water.setFoamHardness(0.6f);

        water.setWaterHeight(-10);
    }

    public DirectionalLight getSun() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(lightDir);
        sun.setColor(ColorRGBA.White.clone().multLocal(1.7f));
        return sun;
    }

    public Spatial getSky() {
        Spatial sky = SkyFactory.createSky(app.getAssetManager(), "Textures/FullskiesSunset0068.dds", false);
        sky.setLocalScale(350);
        return sky;
    }
    
}
