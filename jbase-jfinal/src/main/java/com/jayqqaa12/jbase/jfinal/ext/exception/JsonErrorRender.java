package com.jayqqaa12.jbase.jfinal.ext.exception;
import com.jayqqaa12.jbase.exception.ErrorCode;
import com.jayqqaa12.jbase.jfinal.ext.model.vo.SendJson;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import com.jfinal.render.RenderFactory;

import java.io.IOException;
import java.io.PrintWriter;

public class JsonErrorRender extends Render {

    protected static final String contentType = "application/model; charset=" + getEncoding();


    protected int errorCode;

    public JsonErrorRender(int errorCode) {
        this.errorCode = errorCode;
    }

    public void render() {

        // render with view
        String view = getView();
        if (view != null) {
            RenderFactory.me().getRender(view).setContext(request, response).render();
            return;
        }

        // render with html content
        PrintWriter writer = null;
        try {
            response.setContentType(contentType);
            writer = response.getWriter();
            writer.write(getErrorHtml().toJson());
            writer.flush();
        } catch (IOException e) {
            throw new RenderException(e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public SendJson getErrorHtml() {

        if (errorCode == 404)
            return new SendJson(ErrorCode.NOT_EXSIT_INTERFACE_ERROR.code, ErrorCode.NOT_EXSIT_INTERFACE_ERROR.msg);
        if (errorCode == 500)
            return new SendJson(ErrorCode.SERVER_ERROR.code, ErrorCode.SERVER_ERROR.msg);
        if (errorCode == 401)
            return new SendJson(ErrorCode.UNAUTHORIZED.code, ErrorCode.UNAUTHORIZED.msg);
        if (errorCode == 403)
            return new SendJson(ErrorCode.FORBINDDEN.code, ErrorCode.FORBINDDEN.msg);

        return new SendJson(ErrorCode.SERVER_ERROR.code, "UNKNOWN ERROR");
    }


}
