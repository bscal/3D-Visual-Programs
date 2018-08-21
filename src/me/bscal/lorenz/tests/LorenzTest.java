package me.bscal.lorenz.tests;

import java.util.ArrayList;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

public class LorenzTest extends SimpleApplication {

	private static long id = 0;
	private float period = 0.0f;

	// Default 28, 10, 8/3
	final float sigma = 28.0f, rho = 10.0f, beta = 8.0f / 3.0f;
	float x = 0.01f, y = 0.01f, z = 0.01f;
	float r = 255;
	float g = 255;
	float b = 255;

	ArrayList<Geometry> points = new ArrayList<Geometry>();

	@Override
	public void simpleInitApp() {
		this.flyCam.setMoveSpeed(10.0f);
	}

	public void plotLine(Vector3f[] lineVerticies, ColorRGBA lineColor) {
		Mesh m = new Mesh();
		m.setMode(Mesh.Mode.Lines);
		m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(lineVerticies));

		short[] indexes = new short[2 * lineVerticies.length]; // Indexes are in pairs, from a vertex and to a vertex

		for (short i = 0; i < lineVerticies.length - 1; i++) {
			indexes[2 * i] = i;
			indexes[2 * i + 1] = (short) (i + 1);
		}

		m.setBuffer(VertexBuffer.Type.Index, 2, indexes);
		m.updateBound();
		m.updateCounts();

		Geometry geo = new Geometry("line", m);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.getAdditionalRenderState().setLineWidth(2.0f);
		mat.setColor("Color", lineColor);
		geo.setMaterial(mat);

		rootNode.attachChild(geo);
	}

	ColorRGBA c = new ColorRGBA();

	/* Use the main event loop to trigger repeating actions. */
	@Override
	public void simpleUpdate(float tpf) {
		// rootNode.detachAllChildren();
		// if (points.size() > 1000) {
		// rootNode.detachChildNamed(points.get(0).getName());
		// points.remove(0);
		// }
		Vector3f oldLocation = new Vector3f().set(x, y, z);

		float dx = (rho * (y - x)) * tpf;
		float dy = (x * (sigma - z) - y) * tpf;
		float dz = (x * y - beta * z) * tpf;
		x += dx;
		y += dy;
		z += dz;

		Vector3f location = new Vector3f().set(x, y, z);

		Vector3f[] lineVerticies = { oldLocation, location };
		plotLine(lineVerticies, c);

		// Sphere s = new Sphere(20, 20, 0.05f);
		// Geometry geo = new Geometry("point" + id++, s);
		// geo.setLocalTranslation(location);
		// Material mat = new Material(assetManager,
		// "Common/MatDefs/Misc/Unshaded.j3md");
		// mat.setColor("Color", c);
		// geo.setMaterial(mat);
		// points.add(geo);
		//
		// rootNode.attachChild(geo);

		period += tpf;
		if (period > 1.0f) {
			period = 0.0f;
			c = ColorRGBA.randomColor();
			for (Geometry g : points)
				g.getMaterial().setColor("Color", c);
		}

	}

}
