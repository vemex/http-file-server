package com.vemex.http.file.server

import org.apache.tika.Tika
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.util.comparator.Comparators
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.io.File
import java.nio.file.Path
import java.text.DateFormat
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


/**
 *
 * @author wangweiwei
 * @date 2021/12/24 1:53 下午
 */
@Configuration
class RouterConfig {

    @Bean
    fun routerFunction(): RouterFunction<*> {
        val fileScanner = FileScanner.createFileScanner("/Users/wangweiwei/Documents/k8s")
        return route(GET("/**"), HandlerFunction {
            val requestPath = it.requestPath().toString();
            val resource = fileScanner.getResource(requestPath)
            if (resource != null) {
                val tika = Tika()
                val mimeType: String = tika.detect(resource.file)
                return@HandlerFunction ServerResponse.ok().contentType(MediaType.parseMediaType(mimeType))
                    .body(Mono.just(resource), Resource::class.java)

            }
            val fileScanner1 = fileScanner.get(requestPath)
            if (fileScanner1 != null) {
                return@HandlerFunction ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(
                    Mono.just(fileScanner1.htmlList()),
                    String::class.java
                );
            }
            ServerResponse.notFound().build();
        })


    }

}

class FileScanner {

    companion object {
        fun createFileScanner(rootPath: String): FileScanner {
            return FileScanner(rootPath);
        }

        private fun contact(strs: List<String>, contactStr: String): String {
            var result = "";
            for (i in (0..strs.size - 1)) {
                if (i < strs.size - 1) {
                    result = result + strs[i] + contactStr;
                } else {
                    result = result + strs[i];
                }
            }
            return result;
        }
    }

    val rootPath: String;
    var file: File;

    private constructor(rootPath: String) {
        this.rootPath = rootPath;
        this.file = File(rootPath);
    }

    fun get(path: String): FileScanner? {
        val fileScanner = createFileScanner(this.rootPath);
        val pathFile = getFile(path)
        if (pathFile.isFile || !pathFile.exists()) {
            return null;
        }
        fileScanner.file = pathFile
        return fileScanner
    }

    private fun getFile(p: String): File {
        return Path.of(rootPath, p).toFile()
    }

    fun getResource(path: String): Resource? {
        val pathFile = getFile(path)
        if (pathFile.isDirectory || !pathFile.exists()) {
            return null;
        }
        return FileSystemResource(pathFile);
    }

    fun htmlList(): String {
        val result = ArrayList<String>();
        if (file.path != rootPath) {
            result.add("<a href='../'>../</a>")
        }
        val rPath = this.file.path.replace(this.rootPath, "");

        val files = ArrayList<File>();
        val listFiles = this.file.listFiles()
        if (listFiles != null) {
            files.addAll(listFiles);
        }
        files.stream().sorted(Comparator { a, b ->
            if (a.isDirectory) {
                if (b.isFile) {
                    return@Comparator 1;
                }
                if (b.isDirectory) {
                    return@Comparator Comparators.comparable<String>().compare(a.name, b.name);
                }
            }
            if (a.isFile) {
                if (b.isDirectory) {
                    return@Comparator -1;
                }
                if (b.isDirectory) {
                    return@Comparator Comparators.comparable<String>().compare(a.name, b.name);
                }
            }

            return@Comparator Comparators.comparable<String>().compare(a.name, b.name);
        }).forEach({
            val dateFormat = DateFormat.getInstance().format(Date(it.lastModified())) + "";
            if (it.isFile) {
                result.add(
                    "<div><span style='display:inline-block;width:200px;'>${dateFormat}</span><a " +
                            "href='$rPath/${
                                it
                                    .name
                            }'>${it.name}</a></div>"
                )
            } else {
                result.add(
                    "<div><span style='display:inline-block;width:200px;'>${dateFormat}</span><a " +
                            "href='$rPath/${
                                it
                                    .name
                            }'>${it.name}/</a></div>"
                )
            }
        })

        val contact = contact(result, "\r\n")
        return "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body>$contact</body>"
    }


}