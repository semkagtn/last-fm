package com.semkagtn.lastfm.utils;

import de.umass.lastfm.CallException;

import java.util.function.BiFunction;

/**
 * Created by semkagtn on 2/14/15.
 */
public class RequestWrapper {

    private static final int REQUEST_REPEATS = 2;

    public static <A1, A2, R> R request(BiFunction<A1, A2, R> request,
                                        A1 arg1, A2 arg2)
            throws RequestException {
        int repeats = 0;
        R response = null;
        boolean responseReceived = false;
        do {
            try {
                repeats++;
                response = request.apply(arg1, arg2);
                responseReceived = true;
            } catch (CallException e) {
                if (repeats == REQUEST_REPEATS) {
                    throw new RequestException();
                }
            }
        } while (!responseReceived);
        return response;
    }

    public static <A1, A2, A3, R> R request(TreeFunction<A1, A2, A3, R> request,
                                            A1 arg1, A2 arg2, A3 arg3)
            throws RequestException {
        int repeats = 0;
        R response = null;
        boolean responseReceived = false;
        do {
            try {
                repeats++;
                response = request.apply(arg1, arg2, arg3);
                responseReceived = true;
            } catch (CallException e) {
                if (repeats == REQUEST_REPEATS) {
                    throw new RequestException();
                }
            }
        } while (!responseReceived);
        return response;
    }

    public static <A1, A2, A3, A4, R> R request(FourFunction<A1, A2, A3, A4, R> request,
                                                A1 arg1, A2 arg2, A3 arg3, A4 arg4)
            throws RequestException {
        int repeats = 0;
        R response = null;
        boolean responseReceived = false;
        do {
            try {
                repeats++;
                response = request.apply(arg1, arg2, arg3, arg4);
                responseReceived = true;
            } catch (CallException e) {
                if (repeats == REQUEST_REPEATS) {
                    throw new RequestException();
                }
            }
        } while (!responseReceived);
        return response;
    }

    public static <A1, A2, A3, A4, A5, R> R request(FiveFunction<A1, A2, A3, A4, A5, R> request,
                                                    A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5)
            throws RequestException {
        int repeats = 0;
        R response = null;
        boolean responseReceived = false;
        do {
            try {
                repeats++;
                response = request.apply(arg1, arg2, arg3, arg4, arg5);
                responseReceived = true;
            } catch (CallException e) {
                if (repeats == REQUEST_REPEATS) {
                    throw new RequestException();
                }
            }
        } while (!responseReceived);
        return response;
    }

    public static <A1, A2, A3, A4, A5, A6, R> R request(SixFunction<A1, A2, A3, A4, A5, A6, R> request,
                                                        A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5, A6 arg6)
            throws RequestException {
        int repeats = 0;
        R response = null;
        boolean responseReceived = false;
        do {
            try {
                repeats++;
                response = request.apply(arg1, arg2, arg3, arg4, arg5, arg6);
                responseReceived = true;
            } catch (CallException e) {
                if (repeats == REQUEST_REPEATS) {
                    throw new RequestException();
                }
            }
        } while (!responseReceived);
        return response;
    }

    @FunctionalInterface
    public interface TreeFunction<A1, A2, A3, R> {
        R apply(A1 arg1, A2 arg2, A3 arg3);
    }

    @FunctionalInterface
    public interface FourFunction<A1, A2, A3, A4, R> {
        R apply(A1 arg1, A2 arg2, A3 arg3, A4 arg4);
    }

    @FunctionalInterface
    public interface FiveFunction<A1, A2, A3, A4, A5, R> {
        R apply(A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5);
    }

    @FunctionalInterface
    public interface SixFunction<A1, A2, A3, A4, A5, A6, R> {
        R apply(A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5, A6 arg6);
    }

    public static class RequestException extends Exception {

    }

    private RequestWrapper() {

    }
}
