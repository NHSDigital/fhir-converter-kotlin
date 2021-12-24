package net.nhsd.fhir.converter.model

import ca.uhn.fhir.context.FhirVersionEnum
import net.nhsd.fhir.converter.getResourceType
import org.hl7.fhir.instance.model.api.IBaseResource
import org.springframework.http.MediaType


//typealias FhirContent = Pair<MediaType, FhirVersionEnum>

data class FhirContent(
    val resource: String,
    val mediaType: MediaType,
    val fhirVersion: FhirVersionEnum,
    val resourceType: Class<out IBaseResource> = getResourceType(resource, mediaType, fhirVersion)
)

