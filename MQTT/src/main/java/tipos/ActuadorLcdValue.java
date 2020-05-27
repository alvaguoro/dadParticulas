package tipos;

public class ActuadorLcdValue {
	private int idActuador_LCD_value;
	private int idActuador;
	private String mensaje;
	private Long timestamp;
	
	public ActuadorLcdValue() {
		super();
	}

	public ActuadorLcdValue(int idActuador_LCD_value, int idActuador, String mensaje, Long timestamp) {
		super();
		this.idActuador_LCD_value = idActuador_LCD_value;
		this.idActuador = idActuador;
		this.mensaje = mensaje;
		this.timestamp = timestamp;
	}

	public int getIdActuador_LCD_value() {
		return idActuador_LCD_value;
	}

	public void setIdActuador_LCD_value(int idActuador_LCD_value) {
		this.idActuador_LCD_value = idActuador_LCD_value;
	}

	public int getIdActuador() {
		return idActuador;
	}

	public void setIdActuador(int idActuador) {
		this.idActuador = idActuador;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idActuador;
		result = prime * result + idActuador_LCD_value;
		result = prime * result + ((mensaje == null) ? 0 : mensaje.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
		ActuadorLcdValue other = (ActuadorLcdValue) obj;
		if (idActuador != other.idActuador)
			return false;
		if (idActuador_LCD_value != other.idActuador_LCD_value)
			return false;
		if (mensaje == null) {
			if (other.mensaje != null)
				return false;
		} else if (!mensaje.equals(other.mensaje))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActuadorLcdValue [idActuador_LCD_value=" + idActuador_LCD_value + ", idActuador=" + idActuador
				+ ", mensaje=" + mensaje + ", timestamp=" + timestamp + "]";
	}
	
	
	
}
