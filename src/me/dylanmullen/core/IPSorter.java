package me.dylanmullen.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPSorter
{

	private String file;
	private Pattern ipPat = Pattern.compile(
			"\\b[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,|(\\W|\\d|_)*\\s)+[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,|(\\W|\\d|_)*\\s)+[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,|(\\W|\\d|_)*\\s)+[0-9]{1,3}\\b");

	private boolean ports;
	private Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args)
	{
		new IPSorter("ip-list.txt", (args.length == 0 ? true : (args[0].equalsIgnoreCase("true") ? true : false)));
	}

	public IPSorter(String fileName, boolean ports)
	{
		this.file = fileName;
		this.ports = ports;
		load();
	}

	private void load()
	{
		List<String> ips = new ArrayList<>();

		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(file)));

			String currentLine = "";

			while ((currentLine = br.readLine()) != null)
			{
				Matcher mat = ipPat.matcher(currentLine);
				if (mat.find())
				{
					ips.add(currentLine);
				}
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (br != null)
					br.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		try
		{
			System.out.println("Your Localhost IP Address: " + InetAddress.getLocalHost().getHostAddress());
			
			StringBuilder bb = new StringBuilder();
			
			for(String ip : ips)
			{
				bb.append((ports ? ip : ip.split(";")[0]) + "\n");
			}
			
			System.out.println("IP Adresses found: \n" + bb.toString());
			
		} catch (UnknownHostException e)
		{
			System.err.println("Failed to get Clients IP!");
			return;
		}
		
		boolean ended = false;
		do
		{
			sort(ips);
			
			if(scanner.nextLine().equalsIgnoreCase("close"))
				ended = true;
		}
		while(ended == false);
	}
	
	private void sort(List<String> ips)
	{
		boolean selected = false;
		int port = -1;
			
		do
		{
			System.out.println("What port would you like to sort?");
			try
			{
				port = Math.abs(Integer.parseInt(scanner.nextLine()));
				selected = true;
			}
			catch(NumberFormatException e)
			{
				System.err.println("You must selected a whole number!");
				continue;
			}
		}
		while(selected == false);
		
		StringBuilder bb = new StringBuilder();
		for(String s : ips)
		{
			if(Integer.parseInt(s.split(":")[1]) == port)
				bb.append(s.split(":")[0] + "\n");
		}
		
		System.out.println((bb.length() == 0 ? "There was no IPs for that Port!" : bb.toString()));
		System.out.println("Press enter or type \'close\' to end the application!");
	}

}
