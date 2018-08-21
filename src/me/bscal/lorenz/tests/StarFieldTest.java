package me.bscal.lorenz.tests;

import java.util.ArrayList;
import java.util.Random;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.BufferUtils;

public class StarFieldTest extends SimpleApplication {

	private static long id = 0;
	private float period = 0.0f;

	final float START = -999.0f;
	final float END = 250.0f;
	final float SPEED = 100.0f * 4.0f;
	final int NUMBER_OF_STARS = 1;
	final float WIDTH = 1400.0f;
	final float HEIGHT = 800.0f;

	ArrayList<Geometry> stars = new ArrayList<Geometry>();
	ArrayList<Geometry> lines = new ArrayList<Geometry>();

	@Override
	public void simpleInitApp() {
		this.flyCam.setMoveSpeed(10.0f);
	}

	@Override
	public void simpleUpdate(float tpf) {
		for (int i = 0; i < stars.size(); i++) {
			/*
			 * Gets stars and their respective lines.
			 * Deletes if necessary or updates there position based on speed
			 */
			Geometry sg = stars.get(i);
			Geometry lg = lines.get(i);
			Vector3f t = sg.getLocalTranslation().add(0, 0, SPEED * tpf);
			Vector3f lt = lg.getLocalTranslation().add(0, 0, SPEED * tpf);
			
			if (t.getZ() > END) {
				rootNode.detachChildNamed(sg.getName());
				rootNode.detachChildNamed(lg.getName());
				stars.remove(sg);
				lines.remove(lg);
				return;
			}
			sg.setLocalTranslation(t);
			lg.setLocalTranslation(lt);
		}
		
		/*
		 * Used to control speed at which stars are created.
		 */
		period += tpf;
		if (period > 0.01f) {
			period = 0.0f;
			for (int i = 0; i < NUMBER_OF_STARS; i++) {
				/*
				 * Creates Star in form of jME3 Sphere.
				 */
				Random rand = new Random();
				Sphere s = new Sphere(8, 8, rand.nextFloat() + 0.05f);
				Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
				Geometry geo = new Geometry("Sphere" + id++, s);

				float x = (rand.nextFloat() * WIDTH) - WIDTH / 2;
				float y = (rand.nextFloat() * HEIGHT) - HEIGHT / 2;
				Vector3f v = new Vector3f(x, y, START);
				geo.setLocalTranslation(v);
				mat.setColor("Color", ColorRGBA.White);
				geo.setMaterial(mat);
				rootNode.attachChild(geo);
				
				stars.add(geo);
				
				/*
				 * Creates lines behinds the star in form of jME3 lines.
				 */
				Vector3f[] lineVerticies = { v.subtract(0, 0, rand.nextFloat() * 16.0f + 4.0f), v };
				
				Mesh m = new Mesh();
				m.setMode(Mesh.Mode.Lines);
				m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(lineVerticies));

				short[] indexes = new short[2 * lineVerticies.length]; // Indexes are in pairs, from a vertex and to a vertex

				for (short j = 0; j < lineVerticies.length - 1; j++) {
					indexes[2 * j] = j;
					indexes[2 * j + 1] = (short) (j + 1);
				}

				m.setBuffer(VertexBuffer.Type.Index, 2, indexes);
				m.updateBound();
				m.updateCounts();

				Geometry lineGeo = new Geometry("line" + id, m);
				Material lineMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
				lineMat.getAdditionalRenderState().setLineWidth(rand.nextFloat() * 2.0f + 1.0f);;
				lineMat.setColor("Color", ColorRGBA.White);
				lineGeo.setMaterial(lineMat);

				rootNode.attachChild(lineGeo);
				
				lines.add(lineGeo);
			}
		}
	}
}
