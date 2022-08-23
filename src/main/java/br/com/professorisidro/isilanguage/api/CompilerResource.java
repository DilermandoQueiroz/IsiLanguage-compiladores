package br.com.professorisidro.isilanguage.api;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import br.com.professorisidro.isilanguage.exceptions.IsiSemanticException;
import br.com.professorisidro.isilanguage.parser.IsiLangLexer;
import br.com.professorisidro.isilanguage.parser.IsiLangParser;

public class CompilerResource extends ServerResource {

    @Post()
    public Representation compile(Representation entity) {
        try {
            if (entity == null) {
                return response("No file provided");
            }

            List<FileItem> items = new RestletFileUpload(new DiskFileItemFactory()).parseRepresentation(entity);
            if (items == null) {
                return response("No file provided");
            }

            Optional<FileItem> opt = items.stream().filter(e -> !e.isFormField()).findFirst();
            if (!opt.isPresent()) {
                return response("No file provided");
            }

            return runCompiler(opt.get());
        } catch (Exception e) {
            e.printStackTrace();
            return response("Generic error: " + e.getMessage());
        }
    }

    private Representation runCompiler(FileItem file) {
        try {
            IsiLangLexer lexer = new IsiLangLexer(CharStreams.fromStream(file.getInputStream()));
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            IsiLangParser parser = new IsiLangParser(tokenStream);

            parser.prog();

            return response(Arrays.asList("Compilation successful"));
        } catch (IsiSemanticException e) {
            String msg = String.format("Semantic error - %s", e.getMessage());
            return response(Arrays.asList(msg));
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("Error - %s", e.getMessage());
            return response(Arrays.asList(msg));
        }
    }

    private Representation response(String error) {
        return new JacksonRepresentation<Response>(new Response(error));
    }

    private Representation response(List<String> messages) {
        return new JacksonRepresentation<Response>(new Response(messages));
    }

}
