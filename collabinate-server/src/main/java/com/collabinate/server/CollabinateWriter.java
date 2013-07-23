package com.collabinate.server;

/**
 * The interface for a Collabinate server that performs write (update)
 * operations.
 * 
 * @author mafuba
 *
 */
public interface CollabinateWriter
{
	/**
	 * Adds an entry to an entity's stream, at the correct chronological
	 * location. This method should be implemented to be as idempotent as
	 * possible, e.g. stream entries with the same exact time and content should
	 * not be duplicated.
	 * 
	 * @param entityId The ID of the entity to which an entry will be added.
	 * This value must not be null.
	 * @param streamEntry The entry to add. This value must not be null.
	 */
	public void addStreamEntry(String entityId, StreamEntry streamEntry);
	
	/**
	 * Adds an entity to the collection of entities a user follows.
	 * 
	 * @param userId The ID of the user that follows the entity.
	 * @param entityId The ID of the entity that the user follows.
	 */
	public void followEntity(String userId, String entityId);
}
