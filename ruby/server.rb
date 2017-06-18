require 'rubygems'
require 'bundler/setup'
require 'mockserver-client'

include MockServer
include MockServer::Model::DSL

def run
  client = MockServerClient.new('127.0.0.1', 1081)

  ex = MockServer::Model::Expectation.new
  ex.request do |req|
    req.path = '/hello'
  end

  ex.response do |resp|
    resp.status_code = 200
    resp.headers << header(name: 'Content-Type', value: 'application/json')
    resp.body = body('{"greet": "world"}')
  end

  client.register(ex)
  client.clear(request('GET', '/hello'))
  gets
end

run
