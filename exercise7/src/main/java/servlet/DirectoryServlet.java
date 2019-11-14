package servlet;

import interfaces.local.DirectoryProvider;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Pavel Efimov
 *
 * Класс DirectoryServlet представляет из себя сервлет, привязанный к URL(../directories).
 * Включает в себя Stateless EJB компонент, который содержит методы для обхода рабочей директории
 * сервера и возврата их в виде списка. В компонент инъекцируется объект класса DirectoryProviderEjb,
 * реализующего Local интерфейс DirectoryProvider.
 * */

@WebServlet(urlPatterns = {"/directories"})
public class DirectoryServlet extends HttpServlet {

    @EJB(beanName = "DirectoryProviderEjb")
    private DirectoryProvider directoryProvider;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("currDir", directoryProvider.getCurrDir());
        req.setAttribute("fileList", directoryProvider.getDirectories());
        req.getRequestDispatcher("/directories.jsp").include(req, resp);
    }
}
