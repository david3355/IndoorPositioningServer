package jager.websocket.dbserver.database;

import java.util.List;

import jager.websocket.dbserver.model.Entity;

public interface Repository<TypeOfEntity, TypeOfID>
{
	void save(TypeOfEntity item);
	List<Entity> findAll();
	void delete(TypeOfID id);
	TypeOfEntity findEntity(TypeOfID id);
}
