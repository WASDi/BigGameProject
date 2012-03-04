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
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
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
    private final Vector3f lightDir = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);
    
    private AssetManager assetManager;
    private Camera terrainLodCamera;
    private Node waterReflectionNode;
    
    private Spatial playerModel;
    private Spatial sandGuyModel;

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
        return playerModel; //there should only be one player. No need to clone it
    }
    
    public void resetPlayerTranslations(){
        playerModel.setLocalTranslation(0, -1.95f, -1.6f); //centers the player model
        playerModel.setLocalScale(.5f); //fixes the scale
    }
    
    public Spatial getSandGuyModel(){
        if(sandGuyModel==null){
            sandGuyModel = assetManager.loadModel("Models/Characters/Sandguy.j3o");
            sandGuyModel.setLocalScale(.04f); //It's a big model
        }
        return sandGuyModel.clone(); //use clone since there will be many sand people.
    }
    
    public Spatial getRockModel(){
        Spatial rock = assetManager.loadModel("Models/Rock/roca.j3o");
//        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
//        mat.setTexture("DiffuseMap", assetManager.loadTexture("Models/Rock/roca.png"));
//        mat.setTexture("NormalMap", assetManager.loadTexture("Models/Rock/roca.reflejo.jpg"));
//        mat.setFloat("Shininess", 55f);
//        rock.setMaterial(mat);
        return rock;
    }
    
    public FilterPostProcessor getFilter(){
        //TODO save a variable for filter like the other get-methods.
        FilterPostProcessor filter = new FilterPostProcessor(assetManager);

        BloomFilter bloom = new BloomFilter();
        bloom.setExposurePower(55);
        bloom.setBloomIntensity(1.0f);
        filter.addFilter(bloom);
        
        LightScatteringFilter lsf = new LightScatteringFilter(lightDir.mult(-300));
        lsf.setLightDensity(1.0f);
        filter.addFilter(lsf);
        
        DepthOfFieldFilter dof = new DepthOfFieldFilter();
        dof.setFocusDistance(0);
        dof.setFocusRange(100);
        filter.addFilter(dof);
        
        return filter;
    }
    
    public Spatial getShipModel(){
        //TODO replace with real model
        Box b = new Box(Vector3f.ZERO, 2, .5f, 1);
        Geometry ship = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        ship.setMaterial(mat);
        return ship;
    }
    
    private void initPlayer(){
        playerModel = assetManager.loadModel("Models/Characters/Player.j3o");
        resetPlayerTranslations();
    }
    
    private void initTerrain(){
        if(terrainLodCamera==null)
            throw new NullPointerException("Must call setTerrainCamera first");
        // Terrain material
        Material matTerrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        matTerrain.setBoolean("useTriPlanarMapping", false);
        matTerrain.setFloat("Shininess", .5f);

        // Alpha map
        matTerrain.setTexture("AlphaMap", assetManager.loadTexture("Textures/Terrain/alpha.png"));

        // Textures
        Texture grass = assetManager.loadTexture("Textures/Terrain/grass.jpg");
        grass.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_1", grass);
        matTerrain.setFloat("DiffuseMap_1_scale", 64);

        Texture road = assetManager.loadTexture("Textures/Terrain/road.jpg");
        road.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap", road);
        matTerrain.setFloat("DiffuseMap_0_scale", 128);

        Texture snow = assetManager.loadTexture("Textures/Terrain/snow.png");
        snow.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_2", snow);
        matTerrain.setFloat("DiffuseMap_2_scale", 128);

        Texture sand = assetManager.loadTexture("Textures/Terrain/sand.png");
        sand.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_3", sand);
        matTerrain.setFloat("DiffuseMap_3_scale", 64);

//        Texture normalMap0 = assetManager.loadTexture("Textures/Terrain/splat/grass_normal.jpg");
//        normalMap0.setWrap(WrapMode.Repeat);
//        Texture normalMap1 = assetManager.loadTexture("Textures/Terrain/splat/dirt_normal.png");
//        normalMap1.setWrap(WrapMode.Repeat);
//        Texture normalMap2 = assetManager.loadTexture("Textures/Terrain/splat/road_normal.png");
//        normalMap2.setWrap(WrapMode.Repeat);
//        matTerrain.setTexture("NormalMap", normalMap0);
//        matTerrain.setTexture("NormalMap_1", normalMap2);
//        matTerrain.setTexture("NormalMap_2", normalMap2);
//        matTerrain.setTexture("NormalMap_3", normalMap2);

        // Heightmap
        AbstractHeightMap heightmap = null;
        Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/heightmap.png");
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();
        heightmap.smooth(1f, 4);

        terrain = new TerrainQuad("my terrain", 65, 1025, heightmap.getHeightMap());
        //, new LodPerspectiveCalculatorFactory(getCamera(), 4)); // add this in to see it use entropy for LOD calculations
        TerrainLodControl control = new TerrainLodControl(terrain, terrainLodCamera);
        control.setLodCalculator( new DistanceLodCalculator(65, 2.7f) ); // patch size, and a multiplier
        terrain.addControl(control);
        terrain.setMaterial(matTerrain);
        terrain.setLocalTranslation(0, -52, 0);
        terrain.setLocalScale(1f, .8f, 1f);
    }

    private void initWater() {
        if(waterReflectionNode==null)
            throw new NullPointerException("Must call setWaterNode first");
        water = new WaterFilter(waterReflectionNode, lightDir);

        waterFilter = new FilterPostProcessor(assetManager);

        //TODO separate loading of filters into another method
        waterFilter.addFilter(water);
//        BloomFilter bloom = new BloomFilter();
//        //bloom.getE
//        bloom.setExposurePower(55);
//        bloom.setBloomIntensity(1.0f);
//        waterFilter.addFilter(bloom);
//        LightScatteringFilter lsf = new LightScatteringFilter(lightDir.mult(-300));
//        lsf.setLightDensity(1.0f);
//        waterFilter.addFilter(lsf);
//        DepthOfFieldFilter dof = new DepthOfFieldFilter();
//        dof.setFocusDistance(0);
//        dof.setFocusRange(100);
//        waterFilter.addFilter(dof);


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
