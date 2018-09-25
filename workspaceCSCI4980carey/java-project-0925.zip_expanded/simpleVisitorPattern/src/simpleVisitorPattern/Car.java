package simpleVisitorPattern;

import simpleVisitorPattern.part.Body;
import simpleVisitorPattern.part.Engine;
import simpleVisitorPattern.part.Wheel;
import simpleVisitorPattern.visitor.CarPartVisitor;

class Car {
	Wheel wheel = new Wheel("Unknown");
	Body body = new Body("Unknown");
	Engine engine = new Engine("Unknown");

	public void accept(CarPartVisitor visitor) {
		wheel.accept(visitor);
		engine.accept(visitor);
		body.accept(visitor);
	}
}