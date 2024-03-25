package pe.gob.muniveintiseisdeoctubre.servicios.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class Constants {

    @Value("${project.upload-path}")
    public String uploadPath;

    @Value("${project.upload-report-path}")
    public String uploadReportPath;

    @Value("${project.upload-advertisement-path}")
    public String uploadAdvertisementPath;

    @Value("${project.upload-url}")
    public String uploadUrl;

    @Value("${project.upload-report-url}")
    public String uploadReportUrl;

    @Value("${project.upload-advertisement-url}")
    public String uploadAdvertisementUrl;

    @Value("${project.build-version}")
    public String buildVersion;

    @Value("${project.municipalidad-name}")
    public String nameMunicipalidad;

    @Value("${project.municipalidad-shortname}")
    public String shortnameMunicipalidad;

    @Value("${project.municipalidad-email}")
    public String emailMunicipalidad;

    @Value("${project.base-url}")
    public String baseurl;

    @Value("${project.mail-url}")
    public String mailurl;

    @Value("${project.service-account-path}")
    public String serviceAccountPath;

    @Value("${project.env}")
    public String env;

    public enum UserType {
        AGENT,
        CITIZEN
    }

    public enum ReportStatus {
        PENDING,
        ATTENDED,
        DISCARDED
    }
}
