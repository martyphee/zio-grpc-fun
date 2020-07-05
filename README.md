```bash
grpcurl -plaintext -proto ./protos/src/main/protobuf/greeter.proto -d @ localhost:9090 examples.Greeter/Greet <<EOM
{
"name": "Marty"
}
EOM
{
  "resp": "hello Marty"
}
```
