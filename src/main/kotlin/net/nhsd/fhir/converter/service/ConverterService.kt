package net.nhsd.fhir.converter.service

import ca.uhn.fhir.context.FhirVersionEnum
import ca.uhn.fhir.context.FhirVersionEnum.DSTU3
import ca.uhn.fhir.context.FhirVersionEnum.R4
import net.nhsd.fhir.converter.Converter
import net.nhsd.fhir.converter.FhirParser
import net.nhsd.fhir.converter.Transformer
import net.nhsd.fhir.converter.getResourceType
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
class ConverterService(
    private val fhirParser: FhirParser,
    private val transformer: Transformer,
    private val converter: Converter,
) {

    fun convert(resource: String, mediaType: MediaType, fhirVersion: FhirVersionEnum): String {
        val resourceTyped = getResourceType(resource, mediaType, fhirVersion)
        val targetVersion = if (fhirVersion == R4) DSTU3 else R4

        val parsed = fhirParser.parse(resource, resourceTyped, mediaType)
        val converted = converter.convert(parsed, fhirVersion)
        val transformed = transformer.transform(converted, targetVersion)

        return fhirParser.encode(transformed, mediaType, targetVersion)
    }
}
