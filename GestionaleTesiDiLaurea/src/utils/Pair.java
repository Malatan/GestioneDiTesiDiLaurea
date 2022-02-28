package utils;

public class Pair<U, V> {
	public final U first;
	public final V second;

	private Pair(U first, V second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Pair<?, ?> pair = (Pair<?, ?>) o;
		if (!first.equals(pair.first)) {
			return false;
		}
		return second.equals(pair.second);
	}

	@Override
	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	// Factory method
	public static <U, V> Pair<U, V> of(U a, V b) {
		return new Pair<>(a, b);
	}
}