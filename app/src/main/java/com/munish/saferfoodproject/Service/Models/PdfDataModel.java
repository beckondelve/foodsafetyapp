package com.munish.saferfoodproject.Service.Models;

import java.util.ArrayList;
import java.util.List;

public class PdfDataModel {
   List<pdfNameModel> pdfNameModels;

    public PdfDataModel(List<pdfNameModel> pdfNameModels) {
        this.pdfNameModels = pdfNameModels;
    }

    public List<pdfNameModel> getPdfNameModels() {
        return pdfNameModels;
    }
}
