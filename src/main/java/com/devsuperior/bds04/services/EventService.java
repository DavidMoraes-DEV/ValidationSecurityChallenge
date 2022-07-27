package com.devsuperior.bds04.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.repositories.EventRepository;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;

@Service
public class EventService {

	@Autowired
	private EventRepository repository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Transactional
	public EventDTO update(Long id, EventDTO eventDTO) {

		try {
			Event event = repository.getOne(id);
			event.setName(eventDTO.getName());
			event.setDate(eventDTO.getDate());
			event.setUrl(eventDTO.getUrl());

			Optional<City> obj = cityRepository.findById(eventDTO.getCityId());
			event.setCity(obj.get());

			event = repository.save(event);

			return new EventDTO(event);
			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Impossível concluir a atualização: Id " + id + " - Inexistente");
		}
	}
}
