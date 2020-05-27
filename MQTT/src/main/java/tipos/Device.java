package tipos;

public class Device {
	private Integer idDevice;
	private String ip;
	private String name;
	private Integer idUsuario;
	private Long InitialTimestamp;
	private String ubicacion;
	
	
	public Device() {
		super();
	}




	public Device(Integer idDevice, String ip, String name, Integer idUsuario, Long initialTimestamp,
			String ubicacion) {
		super();
		this.idDevice = idDevice;
		this.ip = ip;
		this.name = name;
		this.idUsuario = idUsuario;
		InitialTimestamp = initialTimestamp;
		this.ubicacion = ubicacion;
	}




	public Integer getIdDevice() {
		return idDevice;
	}




	public void setIdDevice(Integer idDevice) {
		this.idDevice = idDevice;
	}




	public String getIp() {
		return ip;
	}




	public void setIp(String ip) {
		this.ip = ip;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public Integer getIdUsuario() {
		return idUsuario;
	}




	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}




	public Long getInitialTimestamp() {
		return InitialTimestamp;
	}




	public void setInitialTimestamp(Long initialTimestamp) {
		InitialTimestamp = initialTimestamp;
	}




	public String getUbicacion() {
		return ubicacion;
	}




	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((InitialTimestamp == null) ? 0 : InitialTimestamp.hashCode());
		result = prime * result + ((idDevice == null) ? 0 : idDevice.hashCode());
		result = prime * result + ((idUsuario == null) ? 0 : idUsuario.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((ubicacion == null) ? 0 : ubicacion.hashCode());
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
		Device other = (Device) obj;
		if (InitialTimestamp == null) {
			if (other.InitialTimestamp != null)
				return false;
		} else if (!InitialTimestamp.equals(other.InitialTimestamp))
			return false;
		if (idDevice == null) {
			if (other.idDevice != null)
				return false;
		} else if (!idDevice.equals(other.idDevice))
			return false;
		if (idUsuario == null) {
			if (other.idUsuario != null)
				return false;
		} else if (!idUsuario.equals(other.idUsuario))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (ubicacion == null) {
			if (other.ubicacion != null)
				return false;
		} else if (!ubicacion.equals(other.ubicacion))
			return false;
		return true;
	}




	@Override
	public String toString() {
		return "Device [idDevice=" + idDevice + ", ip=" + ip + ", name=" + name + ", idUsuario=" + idUsuario
				+ ", InitialTimestamp=" + InitialTimestamp + ", ubicacion=" + ubicacion + "]";
	}
	
	
	
}
