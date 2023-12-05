package com.epsoftandroid.demo.utils

import java.io.IOException

class EWException(message: String) : Exception(message)

class NoInternetException(message: String) : IOException(message)

class ApiException(message: String) : IOException(message)