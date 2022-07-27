package com.devsuperior.bds04.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.services.exceptions.DataBaseException;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;

@Service
public class CityService {

	@Autowired
	private CityRepository repository;
	
	@Transactional(readOnly = true)
	public List<CityDTO> findAll() {
		
		List<City> list = repository.findAll(Sort.by("name"));
		
		return list.stream().map(x -> new CityDTO(x)).collect(Collectors.toList());		
	}
	
	@Transactional
	public CityDTO insert(CityDTO cityDTO) {
		
		City entity = new City(cityDTO.getId(), cityDTO.getName());
		entity = repository.save(entity);
		
		return new CityDTO(entity);
	}
	
	public void delete(Long id) {

		try {
			repository.deleteById(id);
		} catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Impossível concluir a deleção: Id " + id + " - Inexistente");
		} catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Não é permitido realizar essa deleção: Violação de Integridade Conceitual");
		}
	}
}
