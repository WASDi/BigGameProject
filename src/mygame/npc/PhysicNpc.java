package mygame.npc;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.quest.Quest;

/**
 *
 * @author wasd
 */
public class PhysicNpc extends CharacterControl implements Npc{
    
    private Node node;
    private Vector3f arrowTranslation;
    private float spawnx, spawnz; //the x and y coordinate where the Npc is spawned
    private Vector2f walkTo;
    private Vector3f walkDir = new Vector3f();
    private final float maxWalkDistance = 20f;
    private long nextWalk;

    public PhysicNpc(float sizex, float sizey, Node node) {
        super(new CapsuleCollisionShape(sizex, sizey), .1f);
        this.node = node;
        arrowTranslation = new Vector3f(0, sizey+1f, 0);
    }

    public String talk() {
        return "I am "+node.getName();
    }

    public Vector3f getPosition() {
        return getPhysicsLocation();
    }

    public void onTargeted(Spatial arrow) {
        arrow.setLocalTranslation(arrowTranslation);
        node.attachChild(arrow);
    }

    public void setPosition(float x, float y, float z) {
        spawnx=x;
        spawnz=z;
        setPhysicsLocation(new Vector3f(x, y, z));
        prepareNextWalk();
    }

    public void lookAt(float xlook, float zlook) {
        setViewDirection(getPosition().add(xlook, 0, zlook));
    }

    public Node getNode() {
        return node;
    }
    
    public String getName() {
        return "NAME_NOT_IMPLEMENTED";
    }

    public void addQuest(Quest quest) {
        //TODO
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void questMarkerUpdate(boolean add) {
        //TODO
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(float tpf) {
        //Maybe Npc/enemies should have different behaviours
        //Some should be neutral and attack when being attacked. Some should have an aggro-radius
        //
        //The default behaviour is that they should walk toward a nearby point and stop for a while
        //Then pick a new random point while making sure not to walk too far away from the start location
        
        if(walkTo!=null){
            //walk towards walkTo
            walkDir.set(walkTo.x-getPhysicsLocation().x, 0,
                    walkTo.y-getPhysicsLocation().z).normalizeLocal().multLocal(.1f);
            setWalkDirection(walkDir);
            setViewDirection(walkDir);
            
            if(FastMath.abs(getPhysicsLocation().x-walkTo.x)<1f && FastMath.abs(getPhysicsLocation().z-walkTo.y)<1f){
                //finished walking
                prepareNextWalk();
            }
        }
        else if (System.currentTimeMillis() > nextWalk){
            walkTo = getWalkPoint();
        }
        
        super.update(tpf);
    }
    
    private Vector2f getPointNearSpawn(){
        Vector2f point = new Vector2f(FastMath.rand.nextFloat(), FastMath.rand.nextFloat());
        point.normalizeLocal();
        point.set(spawnx+point.x*maxWalkDistance*FastMath.rand.nextFloat(),
                  spawnz+point.y*maxWalkDistance*FastMath.rand.nextFloat());
        return point;
    }
    
    /**
     * Gets a point that is within 'maxWalkDistance' from spawn but
     * at least 'maxWalkDistance/2' from where the player currently is
     * @return A Vector2f with the x,z coordinates
     */
    private Vector2f getWalkPoint(){
        Vector2f pos = new Vector2f(getPhysicsLocation().x, getPhysicsLocation().z);
        while(true){
            Vector2f point = getPointNearSpawn();
            if(point.distance(pos)>maxWalkDistance/2)
                return point;
        }
    }
    
    private void prepareNextWalk(){
        walkTo = null;
        //wait 3 to 6 seconds until next walk;
        nextWalk = System.currentTimeMillis()+3000+FastMath.rand.nextInt(3000);
        walkDir.zero();
        setWalkDirection(walkDir);
    }
    
}
