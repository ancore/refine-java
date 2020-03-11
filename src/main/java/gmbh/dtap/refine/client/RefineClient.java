/*
 * MIT License
 *
 * Copyright (c) 2018-2020 DTAP GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gmbh.dtap.refine.client;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class RefineClient implements AutoCloseable {

	private final URL url;
	private final HttpClient httpClient;

	RefineClient(URL url, HttpClient httpClient) {
		this.url = url;
		this.httpClient = httpClient;
	}

	public URL createUrl(String path) {
		try {
			return new URL(url, path);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
		return httpClient.execute(request, responseHandler);
	}

	@Override
	public void close() throws Exception {
		if (httpClient instanceof Closeable) {
			((Closeable) httpClient).close();
		}
	}

	@Override
	public String toString() {
		return "RefineClient{" + "url=" + url + '}';
	}
}
