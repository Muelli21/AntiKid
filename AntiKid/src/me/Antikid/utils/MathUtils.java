package me.Antikid.utils;

import org.bukkit.util.Vector;

public class MathUtils {

    public static double difference(double a, double b) {
	return Math.abs(a - b);
    }

    public static Vector rotate(Vector input, double degrees) {
	Vector rotVec = new Vector();
	double rx = (input.getX() * Math.cos(degrees)) - (input.getZ() * Math.sin(degrees));
	double rz = (input.getX() * Math.sin(degrees)) + (input.getZ() * Math.cos(degrees));
	rotVec.setX(rx);
	rotVec.setZ(rz);
	return rotVec;
    }

    public static Vector getDirectionVector(float pitch, float yaw) {
	double pitchRadians = Math.toRadians(pitch);
	double yawRadians = Math.toRadians(yaw);

	double sinPitch = Math.sin(pitchRadians);
	double cosPitch = Math.cos(pitchRadians);
	double sinYaw = Math.sin(yawRadians);
	double cosYaw = Math.cos(yawRadians);

	return new Vector(-cosPitch * sinYaw, sinPitch, -cosPitch * cosYaw);
    }
}
