package common;

import java.util.ArrayList;
import java.util.List;

public class Plane
{
	public float collision_radius;

	public int current_waypoint_index;

	public Vectorf2 heading;

	public int id;

	public Vectorf2 new_heading;

	public float new_rotation;

	public Vectorf2 new_waypoint;

	public Vectorf2 position;

	public float repulsion_radius;

	public float rotation;

	public float speed;

	public float turn_speed;

	public List<Vectorf2> waypoints;

	public Plane()
	{
		waypoints = new ArrayList<Vectorf2>();
	}

	public float getCollision_radius() {
		return collision_radius;
	}

	public int getCurrent_waypoint_index() {
		return current_waypoint_index;
	}

	public Vectorf2 getHeading() {
		return heading;
	}

	public int getId() {
		return id;
	}

	@JSONIgnore
	public Vectorf2 getNew_heading() {
		return new_heading;
	}

	@JSONIgnore
	public float getNew_rotation() {
		return new_rotation;
	}

	@JSONIgnore
	public Vectorf2 getNew_waypoint() {
		return new_waypoint;
	}

	public int getPlane_id() {
		return id;
	}

	public Vectorf2 getPosition() {
		return position;
	}

	@JSONIgnore
	public float getRepulsion_radius() {
		return repulsion_radius;
	}

	public float getRotation() {
		return rotation;
	}

	public float getSpeed() {
		return speed;
	}

	public float getTurn_speed() {
		return turn_speed;
	}

	public List<Vectorf2> getWaypoints() {
		return waypoints;
	}

	public void setCollision_radius(float collision_radius) {
		this.collision_radius = collision_radius;
	}

	public void setCurrent_waypoint_index(int current_waypoint_index) {
		this.current_waypoint_index = current_waypoint_index;
	}

	public void setHeading(Vectorf2 heading) {
		this.heading = heading;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNew_heading(Vectorf2 new_heading) {
		this.new_heading = new_heading;
	}

	public void setNew_rotation(float new_rotation) {
		this.new_rotation = new_rotation;
	}

	public void setNew_waypoint(Vectorf2 new_waypoint) {
		this.new_waypoint = new_waypoint;
	}

	public void setPosition(Vectorf2 position) {
		this.position = position;
	}

	public void setRepulsion_radius(float repulsion_radius) {
		this.repulsion_radius = repulsion_radius;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setTurn_speed(float turn_speed) {
		this.turn_speed = turn_speed;
	}

	public void setWaypoints(List<Vectorf2> waypoints) {
		this.waypoints = waypoints;
	}
}
