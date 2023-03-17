
def main():
    with open("index.html") as f:
        index_data = f.read()

        with open("fcl.html") as f_fcl:
            index_data = index_data.replace("FCL_HTML", f_fcl.read())

        with open("nn.html") as f_nn:
            index_data = index_data.replace("NEURAL_NETWORK_HTML", f_nn.read())

        with open("submission.html", "w") as f1:
            f1.write(index_data)

main()

