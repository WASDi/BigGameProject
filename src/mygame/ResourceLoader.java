package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

/**
 * A class used to manage loading of all assets such as terrain, water and models.
 * 
 * TODO make some kind of Options class to manage graphical setting the user has set.
 * 
 * @author wasd
 */
public class ResourceLoader {

    //Use variables to save already loaded assets
    private Spatial skyBox;
    private Spatial spaceBox;
    private TerrainQuad terrain;
    private WaterFilter water;
    private FilterPostProcessor waterFilter;
    private Vector3f lightDir = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);
    
    private AssetManager assetManager;
    private Camera terrainLodCamera;
    private Node waterReflectionNode;
    
    private Spatial playerModel;

    /**
     * @param app Used to get AssetManager for loading assets
     * @param terrainLodCamera The Camera used for LOD calculation
     * @param waterReflectionNode What the water should reflect
     */
    public ResourceLoader(AssetManager assetManager, Camera terrainLodCamera, Node waterReflectionNode) {
        this.assetManager = assetManager;
        this.terrainLodCamera=terrainLodCamera;
        this.waterReflectionNode=waterReflectionNode;
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
    
    public DirectionalLight getSun() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(lightDir);
        sun.setColor(ColorRGBA.White.clone().multLocal(1.7f));
        return sun;
    }
    
    public Spatial getSpace(){
        if(spaceBox==null){
            Texture space = assetManager.loadTexture("Textures/space.jpg");
            Texture earth = assetManager.loadTexture("Textures/earth.jpg");
            spaceBox = SkyFactory.createSky(assetManager, space, space, space, space, space, earth);
        }
        return spaceBox;
    }

    public Spatial getSky() {
        if(skyBox==null){
            skyBox = SkyFactory.createSky(assetManager, "Textures/FullskiesSunset0068.dds", false);
        }
        return skyBox;
    }
    
    public Spatial getPlayerModel(){
        if(playerModel==null){
            initPlayer();
        }
        return playerModel;
    }
    
    public Spatial getRockModel(){
        Spatial rock = assetManager.loadModel("Models/Rock/roca.j3o");
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", assetManager.loadTexture("Models/Rock/roca.png"));
        mat.setTexture("NormalMap", assetManager.loadTexture("Models/Rock/roca.reflejo.jpg"));
        mat.setFloat("Shininess", 55f);
        rock.setMaterial(mat);
        rock.setLocalScale(2f);
        return rock;
    }
    
    private void initPlayer(){
        //Example box as placeholder for player. TODO replace with actual player model
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        playerModel = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        playerModel.setMaterial(mat);
    }

    private void initTerrain() {
        if(terrainLodCamera==null)
            throw new NullPointerException("Must call setTerrainCamera first");
        //TODO Fix texture for my custom heightmap.

        /** 1. Create terrain material and load four textures into it. */
        Material mat_terrain = new Material(assetManager,
                "Common/MatDefs/Terrain/Terrain.j3md");

        /** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
        mat_terrain.setTexture("Alpha", assetManager.loadTexture(
                "Textures/Terrain/alphamap.png"));

        /** 1.2) Add GRASS texture into the red layer (Tex1). */
        Texture grass = assetManager.loadTexture(
                "Textures/Terrain/grass.jpg");
        grass.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex1", grass);
        mat_terrain.setFloat("Tex1Scale", 64f);

        /** 1.3) Add DIRT texture into the green layer (Tex2) */
        Texture dirt = assetManager.loadTexture(
                "Textures/Terrain/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex2", dirt);
        mat_terrain.setFloat("Tex2Scale", 32f);

        /** 1.4) Add ROAD texture into the blue layer (Tex3) */
        Texture rock = assetManager.loadTexture(
                "Textures/Terrain/road.jpg");
        rock.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex3", rock);
        mat_terrain.setFloat("Tex3Scale", 128f);

        /** 2. Create the height map */
        AbstractHeightMap heightmap = null;
        Texture heightMapImage = assetManager.loadTexture(
                "Textures/Terrain/heightmap.png");
//        heightmap = new ImageBasedHeightMap(
//                ImageToAwt.convert(heightMapImage.getImage(), false, true, -1));
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();
        heightmap.smooth(1f, 3);

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
        terrain.setLocalTranslation(0, -52, 0);
        terrain.setLocalScale(1f, .8f, 1f);

        /** 5. The LOD (level of detail) depends on were the camera is: */
        TerrainLodControl control = new TerrainLodControl(terrain, terrainLodCamera);
        terrain.addControl(control);
    }

    private void initWater() {
        if(waterReflectionNode==null)
            throw new NullPointerException("Must call setWaterNode first");
        water = new WaterFilter(waterReflectionNode, lightDir);

        waterFilter = new FilterPostProcessor(assetManager);

        //TODO separate loading of filters into another method
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
        water.setFoamTexture((Texture2D) assetManager.loadTexture("Common/MatDefs/Water/Textures/foam2.jpg")); //foam to foam3
        //water.setNormalScale(0.5f);

        //water.setRefractionConstant(0.25f);
        water.setRefractionStrength(0.2f);
        //water.setFoamHardness(0.6f);

        water.setWaterHeight(-10);
    }
    
}