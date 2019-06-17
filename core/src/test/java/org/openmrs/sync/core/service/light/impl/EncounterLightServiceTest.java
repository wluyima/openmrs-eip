package org.openmrs.sync.core.service.light.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.sync.core.entity.light.EncounterLight;
import org.openmrs.sync.core.entity.light.EncounterTypeLight;
import org.openmrs.sync.core.entity.light.PatientLight;
import org.openmrs.sync.core.repository.OpenMrsRepository;
import org.openmrs.sync.core.service.light.LightServiceNoContext;
import org.openmrs.sync.core.service.light.impl.context.EncounterContext;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class EncounterLightServiceTest {

    @Mock
    private OpenMrsRepository<EncounterLight> repository;

    @Mock
    private LightServiceNoContext<PatientLight> patientService;

    @Mock
    private LightServiceNoContext<EncounterTypeLight> encounterTypeService;

    private EncounterLightService service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        service = new EncounterLightService(repository, patientService, encounterTypeService);
    }

    @Test
    public void getShadowEntity() {
        // Given
        when(patientService.getOrInit("patient")).thenReturn(getPatient());
        when(encounterTypeService.getOrInit("encounterType")).thenReturn(getEncounterType());
        EncounterContext encounterContext = EncounterContext.builder()
                .patientUuid("patient")
                .encounterTypeUuid("encounterType")
                .build();

        // When
        EncounterLight result = service.getShadowEntity("UUID", encounterContext);

        // Then
        assertEquals(getExpectedEncounter(), result);
    }

    private EncounterLight getExpectedEncounter() {
        EncounterLight encounter = new EncounterLight();
        encounter.setUuid("UUID");
        encounter.setDateCreated(LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0));
        encounter.setCreator(1L);
        encounter.setEncounterType(getEncounterType());
        encounter.setEncounterDatetime(LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0));
        encounter.setPatient(getPatient());
        return encounter;
    }

    private PatientLight getPatient() {
        PatientLight patient = new PatientLight();
        patient.setUuid("patient");
        return patient;
    }

    private EncounterTypeLight getEncounterType() {
        EncounterTypeLight encounterType = new EncounterTypeLight();
        encounterType.setUuid("encounterType");
        return encounterType;
    }
}