package com.collabinate.server.resources;

import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.collabinate.server.StreamEntry;
import com.collabinate.server.engine.CollabinateReader;
import com.collabinate.server.engine.CollabinateWriter;
import com.google.common.base.Joiner;

/**
 * Restful resource representing a series of stream entries for an entity.
 * 
 * @author mafuba
 *
 */
public class StreamResource extends ServerResource
{
	// TODO: some much heavier content processing is going to need to happen
	// here to handle different XML and JSON representations of different
	// stream types (e.g. activity streams, OData, raw)
	@Get("json")
	public String getStream()
	{
		// extract necessary information from the context
		CollabinateReader reader = (CollabinateReader)getContext()
				.getAttributes().get("collabinateReader");
		String tenantId = getAttribute("tenantId");
		String entityId = getAttribute("entityId");
		String startString = getQueryValue("start");
		String countString = getQueryValue("count");
		long start = null == startString ? 0 : Long.parseLong(startString);
		int count = null == countString ? DEFAULT_COUNT : 
			Integer.parseInt(countString);
		
		return "{\"items\":[" + Joiner.on(',')
				.join(reader.getStream(tenantId, entityId, start, count))
				+ "]}";
	}
	
	@Post
	public void addEntry(String entryContent)
	{
		CollabinateWriter writer = (CollabinateWriter)getContext()
				.getAttributes().get("collabinateWriter");
		
		if (null == writer)
			throw new IllegalStateException(
					"Context does not contain a CollabinateWriter");
		
		StreamEntry entry = new StreamEntry(null, null, entryContent);
		
		writer.addStreamEntry(getAttribute("tenantId"), 
				getAttribute("entityId"), entry);
		
		// if there is no request entity return empty string with text type
		if (null != entryContent)
			getResponse().setEntity(entry.getContent(), 
					getRequest().getEntity().getMediaType());
		else
			getResponse().setEntity(entry.getContent(), MediaType.TEXT_PLAIN);
		
		setLocationRef(new Reference(getReference()).addSegment(entry.getId()));
		setStatus(Status.SUCCESS_CREATED);
	}
	
	private static final int DEFAULT_COUNT = 20;
}
