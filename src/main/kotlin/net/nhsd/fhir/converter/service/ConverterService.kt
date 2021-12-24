package net.nhsd.fhir.converter.service

import ca.uhn.fhir.context.FhirVersionEnum
import net.nhsd.fhir.converter.Converter
import net.nhsd.fhir.converter.FhirParser
import net.nhsd.fhir.converter.Transformer
import net.nhsd.fhir.converter.model.FhirContent
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
class ConverterService(
    private val fhirParser: FhirParser,
    private val transformer: Transformer,
    private val converter: Converter,
) {

    fun convert(fhirContent: FhirContent, outMediaType: MediaType, outFhirVersion: FhirVersionEnum): String {
        val parsed = fhirParser.parse(fhirContent)

        val inFhirVersion = fhirContent.fhirVersion
        val converted = converter.convert(parsed, inFhirVersion, outFhirVersion)

        val transformed = transformer.transform(converted, outFhirVersion)

        return fhirParser.encode(transformed, outMediaType, outFhirVersion)
    }
}
