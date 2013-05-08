package yar.quadraturin.config;

public class ConfigException extends RuntimeException
{
	public ConfigException(String message) { super(message); }
	public ConfigException(Throwable e) { super(e); }
	public ConfigException(String message, Throwable e) { super(message, e); }
}
