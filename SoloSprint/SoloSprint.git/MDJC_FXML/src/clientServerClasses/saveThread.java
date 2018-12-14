package clientServerClasses;

class saveThread extends Thread
{
	boolean running;
	Server server;
	String filename;
	public saveThread(Server server, String filename)
	{
			this.running = true;
			this.server = server;
			this.filename = filename;
	}
		
	public void setServer(Server server)
	{
		this.server = server;
	}
	
	public void endThread()
	{
		this.running = false;
	}
	
	@Override
	public void run()
	{
		while(running)
		{
			try
			{
				Thread.sleep(2 * 60 * 1000);
			} catch (InterruptedException e)
			{
				server.writeXMLServer(filename);
			}
			if(Thread.interrupted())
			{
				server.writeXMLServer(filename);
			}
			server.writeXMLServer(filename);
		}
	}
}
