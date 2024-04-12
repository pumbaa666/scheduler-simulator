package ch.correvon.scheduler.use;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class ReadInstallationPath
{
	public static String getPath()
	{
		String value = "Path";
		String key = "reg query \"HKEY_LOCAL_MACHINE\\SOFTWARE\\Scheduler\"";

		try
		{
			Process process = Runtime.getRuntime().exec(key);
			StreamReader reader = new StreamReader(process.getInputStream());

			reader.start();
			process.waitFor();
			reader.join();

			String result = reader.getResult();

			int p = result.indexOf(value);
			if(p == -1)
				return null;

			return result.substring(p + value.length()).trim().substring(7); // 7 pour enlever le REG_SZ
		}
		catch(Exception e)
		{
			return null;
		}
	}

	// parser du résultat
	private static class StreamReader extends Thread
	{
		private InputStream is;
		private StringWriter sw;

		StreamReader(InputStream is)
		{
			this.is = is;
			sw = new StringWriter();
		}

		@Override public void run()
		{
			try
			{
				int c;
				while((c = is.read()) != -1)
					sw.write(c);
			}
			catch(IOException e)
			{
			}
		}

		String getResult()
		{
			return sw.toString();
		}
	}

	public static void main(String s[])
	{
		System.out.println(getPath());
	}

}