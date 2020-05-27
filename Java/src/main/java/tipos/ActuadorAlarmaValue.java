package tipos;

public class ActuadorAlarmaValue {
	private int idActuador_alarma_value;
	private int idActuador;
	private float value;
	private long timestamp;
	
	public ActuadorAlarmaValue() {
		super();
	}

	public ActuadorAlarmaValue(int idActuador_alarma_value, int idActuador, float value, long timestamp) {
		super();
		this.idActuador_alarma_value = idActuador_alarma_value;
		this.idActuador = idActuador;
		this.value = value;
		this.timestamp = timestamp;
	}

	public int getIdActuador_alarma_value() {
		return idActuador_alarma_value;
	}

	public void setIdActuador_alarma_value(int idActuador_alarma_value) {
		this.idActuador_alarma_value = idActuador_alarma_value;
	}

	public int getIdActuador() {
		return idActuador;
	}

	public void setIdActuador(int idActuador) {
		this.idActuador = idActuador;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idActuador;
		result = prime * result + idActuador_alarma_value;
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		result = prime * result + Float.floatToIntBits(value);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActuadorAlarmaValue other = (ActuadorAlarmaValue) obj;
		if (idActuador != other.idActuador)
			return false;
		if (idActuador_alarma_value != other.idActuador_alarma_value)
			return false;
		if (timestamp != other.timestamp)
			return false;
		if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActuadorAlarmaValue [idActuador_alarma_value=" + idActuador_alarma_value + ", idActuador=" + idActuador
				+ ", value=" + value + ", timestamp=" + timestamp + "]";
	}
	
	
	
	
}
