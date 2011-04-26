@Typed
class Nbody {

	public static void main(String[] args) {
		long start = System.currentTimeMillis()
		for (int i = 0; i < 65; ++i)
			Nbody.program_main(args, false)
		Nbody.program_main(args, true)
		long total = System.currentTimeMillis() - start
		println "[NBody-Groovy++ Benchmark Result: $total ]"
	}

	public static void program_main(String[] args, boolean isWarm) {
		int n = 50000;

		if (args.length >= 1)
		    n = Integer.parseInt(args[0])

		NBodySystem bodies = new NBodySystem()

		double e = bodies.energy()
		if (isWarm) System.out.printf("%.9f\n", e)
		for (int i = 0; i < n; ++i)
			bodies.advance(0.01d)
		if (isWarm) System.out.printf("%.9f\n", bodies.energy())
	}


	static class NBodySystem {
		private Body[] bodies = [
				Body.sun(),
				Body.jupiter(),
				Body.saturn(),
				Body.uranus(),
				Body.neptune()
		]

		NBodySystem() {

			double px = 0.0d
			double py = 0.0d
			double pz = 0.0d
			for (int i = 0; i < bodies.length; ++i) {
				px += bodies[i].vx * bodies[i].mass
				py += bodies[i].vy * bodies[i].mass
				pz += bodies[i].vz * bodies[i].mass
			}
			bodies[0].offsetMomentum(px, py, pz)
		}

		void advance(double dt) {

			for (int i = 0; i < bodies.length; ++i) {
				Body iBody = bodies[i]
				for (int j = i + 1; j < bodies.length; ++j) {
					def jBody = bodies[j]
                    double dx = iBody.x - jBody.x
					double dy = iBody.y - jBody.y
					double dz = iBody.z - jBody.z

					double dSquared = dx * dx + dy * dy + dz * dz
					double distance = Math.sqrt(dSquared)
					double mag = dt / (dSquared * distance)

					iBody.vx -= dx * jBody.mass * mag
					iBody.vy -= dy * jBody.mass * mag
					iBody.vz -= dz * jBody.mass * mag

					jBody.vx += dx * iBody.mass * mag
					jBody.vy += dy * iBody.mass * mag
					jBody.vz += dz * iBody.mass * mag
				}
			}

			for (Body body: bodies) {
				body.x += dt * body.vx
				body.y += dt * body.vy
				body.z += dt * body.vz
			}
		}

		public double energy() {
			double dx, dy, dz, distance
			double e = 0.0d

			for (int i = 0; i < bodies.length; ++i) {
				Body iBody = bodies[i]
				e += 0.5d * iBody.mass *
						(iBody.vx * iBody.vx
								+ iBody.vy * iBody.vy
								+ iBody.vz * iBody.vz)

				for (int j = i + 1; j < bodies.length; ++j) {
					Body jBody = bodies[j]
					dx = iBody.x - jBody.x
					dy = iBody.y - jBody.y
					dz = iBody.z - jBody.z

					distance = Math.sqrt(dx * dx + dy * dy + dz * dz)
					e -= (iBody.mass * jBody.mass) / distance
				}
			}
			e
		}
	}


	static class Body {
		static final double PI = 3.141592653589793d
		static final double SOLAR_MASS = 4 * PI * PI
		static final double DAYS_PER_YEAR = 365.24d

		public double x, y, z, vx, vy, vz, mass

		static Body jupiter() {
			Body p = new Body()
			p.x = 4.84143144246472090e+00d
			p.y = -1.16032004402742839e+00d
			p.z = -1.03622044471123109e-01d
			p.vx = 1.66007664274403694e-03d * DAYS_PER_YEAR
			p.vy = 7.69901118419740425e-03d * DAYS_PER_YEAR
			p.vz = -6.90460016972063023e-05d * DAYS_PER_YEAR
			p.mass = 9.54791938424326609e-04d * SOLAR_MASS
			p
		}

		static Body saturn() {
			Body p = new Body()
			p.x = 8.34336671824457987e+00d
			p.y = 4.12479856412430479e+00d
			p.z = -4.03523417114321381e-01d
			p.vx = -2.76742510726862411e-03d * DAYS_PER_YEAR
			p.vy = 4.99852801234917238e-03d * DAYS_PER_YEAR
			p.vz = 2.30417297573763929e-05d * DAYS_PER_YEAR
			p.mass = 2.85885980666130812e-04d * SOLAR_MASS
			p
		}

		static Body uranus() {
			Body p = new Body()
			p.x = 1.28943695621391310e+01d
			p.y = -1.51111514016986312e+01d
			p.z = -2.23307578892655734e-01d
			p.vx = 2.96460137564761618e-03d * DAYS_PER_YEAR
			p.vy = 2.37847173959480950e-03d * DAYS_PER_YEAR
			p.vz = -2.96589568540237556e-05d * DAYS_PER_YEAR
			p.mass = 4.36624404335156298e-05d * SOLAR_MASS
			p
		}

		static Body neptune() {
			Body p = new Body()
			p.x = 1.53796971148509165e+01d
			p.y = -2.59193146099879641e+01d
			p.z = 1.79258772950371181e-01d
			p.vx = 2.68067772490389322e-03d * DAYS_PER_YEAR
			p.vy = 1.62824170038242295e-03d * DAYS_PER_YEAR
			p.vz = -9.51592254519715870e-05d * DAYS_PER_YEAR
			p.mass = 5.15138902046611451e-05d * SOLAR_MASS
			p
		}

		static Body sun() {
			Body p = new Body()
			p.mass = SOLAR_MASS
			p
		}

		Body offsetMomentum(double px, double py, double pz) {
			vx = -px / SOLAR_MASS
			vy = -py / SOLAR_MASS
			vz = -pz / SOLAR_MASS
			this
		}
	}
}
