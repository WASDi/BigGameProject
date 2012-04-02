package mygame.npc;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.controls.PlayerControl;
import mygame.quest.Quest;

/**
 *
 * @author wasd
 */
public class PhysicNpc extends CharacterControl implements Npc{
    
    private Node node;
    private Vector3f arrowTranslation;
    private float spawnx, spawnz; //the x and y coordinate where the Npc is spawned
    private Vector3f walkTo;
    private Vector3f walkDir = new Vector3f();
    private final float maxWalkDistance = 20f;
    private long nextWalk;
    private int maxHp;
    private int hp;
    private boolean enraged;
    
    private Vector3f knockback = null;
    private float knockTime = 0f;
    private PlayerControl attacker;

    public PhysicNpc(float sizex, float sizey, Node node, int maxHp) {
        super(new CapsuleCollisionShape(sizex, sizey), .1f);
        this.node = node;
        arrowTranslation = new Vector3f(0, sizey, 0);
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public String talk() {
        if(attacker != null){
            //You have attacked this enemy before and he gets angry when you talk to him
            enrage(attacker, false);
            return "Die!";
        }
        else{
            //Enemy doesn't know about you
            return "Hmm?";
        }
        
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
        return node.getName();
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
        
        if(enraged && attacker!=null){
            if(knockback!=null){
                knockTime+=tpf*1.5f;
                if(knockTime>1){
                    knockback = null;
                }
                else{
                    float percent = 1-knockTime;
                    percent*=.4f;
                    setWalkDirection(knockback.mult(percent));
                }
            }
            else{
                //walk towards player
                walkTo = attacker.getPhysicsLocation();
            }
            //TODO stop being enraged if moving too far from spawn
            //TODO possibly call nearby enemies to join the attack against the player
        }
        
        if(walkTo!=null){
            //walk towards walkTo
            walkDir.set(walkTo.x-getPhysicsLocation().x, 0,
                    walkTo.z-getPhysicsLocation().z).normalizeLocal().multLocal(.1f);
            setWalkDirection(walkDir);
            if(knockback==null)
                setViewDirection(walkDir);
            
            if(!enraged && FastMath.abs(getPhysicsLocation().x-walkTo.x)<1f &&
                    FastMath.abs(getPhysicsLocation().z-walkTo.z)<1f){
                //finished walking, ignores y-axis
                prepareNextWalk();
            }
        }
        else if (System.currentTimeMillis() > nextWalk){
            walkTo = getWalkPoint();
        }
        
        super.update(tpf);
    }
    
    private Vector3f getPointNearSpawn(){
        Vector3f point = new Vector3f(FastMath.rand.nextFloat(), 0, FastMath.rand.nextFloat());
        point.normalizeLocal();
        point.set(spawnx+point.x*maxWalkDistance*FastMath.rand.nextFloat(), 0,
                  spawnz+point.z*maxWalkDistance*FastMath.rand.nextFloat());
        return point;
    }
    
    /**
     * Gets a point that is within 'maxWalkDistance' from spawn but
     * at least 'maxWalkDistance/2' from where the player currently is
     * @return A Vector2f with the x,z coordinates
     */
    private Vector3f getWalkPoint(){
        while(true){
            Vector3f point = getPointNearSpawn();
            if(getPhysicsLocation().distance(point)>maxWalkDistance/2)
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

    public void onAttack(int dmg, Vector3f direction, PlayerControl player) {
        if(hp<=0){
            //already dead
            return;
        }
        hp-=dmg;
        if(hp<=0){
            player.onEmemyDeath(this);
            //die
            //TODO some way for enemies to respawn
            //maybe not remove them from npcList and add to
            //rootNode and physics after a while
            getPhysicsSpace().remove(this);
            node.removeFromParent();
            return;
        }
        knockback = direction;
        knockTime = 0f;
        enrage(player, true);
        jump();
    }
    
    /**
     * Makes the enemy angry
     * @param player The attacker
     * @param hadKnockback If the player got knockbacked it will cancel current movement
     */
    private void enrage(PlayerControl player, boolean hadKnockback){
        attacker = player;
        enraged = true;
        if(hadKnockback){
            //walkTo gets calculated later in the loop
            walkTo = null; 
        }
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }
    
}
