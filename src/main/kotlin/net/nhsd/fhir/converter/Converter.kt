package net.nhsd.fhir.converter

import ca.uhn.fhir.context.FhirVersionEnum
import org.hl7.fhir.convertors.conv30_40.VersionConvertor_30_40
import org.hl7.fhir.instance.model.api.IBaseResource
import org.springframework.stereotype.Component
import org.hl7.fhir.dstu3.model.Resource as R3Resource

@Component
class Converter(private val convertor30to40: VersionConvertor_30_40) {

    fun convert(resource: IBaseResource, version: FhirVersionEnum): IBaseResource {
        return if (FhirVersionEnum.R4 == version) {
            convertor30to40.convertResource(resource as R3Resource)
        } else {
            convertor30to40.convertResource(resource as R3Resource)
        }
    }
}
