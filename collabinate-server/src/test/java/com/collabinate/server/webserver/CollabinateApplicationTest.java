package com.collabinate.server.webserver;

import static org.junit.Assert.*;

import org.junit.Test;
import org.restlet.Application;

import com.collabinate.server.engine.GraphServer;
import com.collabinate.server.webserver.CollabinateApplication;
import com.tinkerpop.blueprints.GraphFactory;
import com.tinkerpop.blueprints.KeyIndexableGraph;

/**
 * Test class for the server application.
 * 
 * @author mafuba
 *
 */
public class CollabinateApplicationTest
{	
	@Test
	public void application_should_be_named_collabinate() throws Exception
	{
		KeyIndexableGraph graph = (KeyIndexableGraph)GraphFactory.open(
				"src/test/resources/graph.properties");
		GraphServer server = new GraphServer(graph);
		Application app = new CollabinateApplication(server, server, null);
		assertEquals("Collabinate", app.getName());
		app.stop();
		graph.shutdown();
	}
}